<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String origen = request.getAttribute("origen").toString();
    String destino = request.getAttribute("destino").toString();
%>
<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
        <meta charset="utf-8">
        <title>Draggable directions</title>
        <style>
            #right-panel {
                font-family: 'Roboto','sans-serif';
                line-height: 30px;
                padding-left: 10px;
            }

            #right-panel select, #right-panel input {
                font-size: 15px;
            }

            #right-panel select {
                width: 100%;
            }

            #right-panel i {
                font-size: 12px;
            }
            html, body {
                height: 100%;
                margin: 0;
                padding: 0;
            }
            #map {
                height: 100%;
                float: left;
                width: 100%;
                height: 100%;
            }
            #right-panel {
                float: right;
                width: 0%;
                height: 100%;
                display: none;
            }
            .panel {
                height: 100%;
                overflow: auto;
            }
        </style>
    </head>
    <body>
        <div id="map"></div>
        <div id="right-panel">
        </div>
        <script>
            var mapa = {
                origen: getOrigen(),
                destino: getDestino(),
                distancia: 0,
                origenPlaceID: '',
                destinoPlaceID: '',
                origenDireccion: '',
                destinoDireccion: '',
                origenLatLng: null,
                destinoLatlng: null
            };
            var cambioNulo = "Cambio nulo";
            var cambioOrigen = "Cambio de origen";
            var cambioDestino = "Cambio de destino";
            var cambio = cambioNulo;

            function buscar() {
                if (cambio === cambioNulo) {
                    mapa = {
                        origen: getOrigen(),
                        destino: getDestino(),
                        distancia: 0,
                        origenPlaceID: '',
                        destinoPlaceID: '',
                        origenDireccion: '',
                        destinoDireccion: '',
                        origenLatLng: null,
                        destinoLatlng: null
                    };
                }
                initMap();
            }

            function initMap() {
                var map = new google.maps.Map(document.getElementById('map'), {});

                var directionsService = new google.maps.DirectionsService;
                var geocoder = new google.maps.Geocoder;
                var directionsDisplay = new google.maps.DirectionsRenderer({
                    draggable: true,
                    map: map
                            //panel: document.getElementById('right-panel')
                });

                //listener para arrastre de origen o destino
                directionsDisplay.addListener('directions_changed', function () {
//                    console.log('directions_changed init');
                    var mapa_tmp = {
                        origen: mapa.origen,
                        destino: mapa.destino,
                        origenPlaceID: mapa.origenPlaceID,
                        destinoPlaceID: mapa.destinoPlaceID,
                        origenDireccion: mapa.origenDireccion,
                        destinoDireccion: mapa.destinoDireccion
                    };

                    computeTotalDistance(directionsDisplay.getDirections());
//                    console.log('directionsDisplay: ');
//                    console.log(directionsDisplay);
//                    console.log(directionsDisplay.directions.request);
                    mapa.origenPlaceID = directionsDisplay.directions.geocoded_waypoints[0].place_id;
                    mapa.destinoPlaceID = directionsDisplay.directions.geocoded_waypoints[1].place_id;

                    if (mapa_tmp.origenPlaceID === '') {
                        cambio = cambioNulo;
                    } else if (mapa_tmp.origenPlaceID !== mapa.origenPlaceID) {
                        cambio = cambioOrigen;
                        geocodePlaceId(geocoder);
                    } else if (mapa_tmp.destinoPlaceID !== mapa.destinoPlaceID) {
                        cambio = cambioDestino;
                        geocodePlaceId(geocoder);
                    }

                    var service = new google.maps.places.PlacesService(map);

                    var request = {placeId: mapa.origenPlaceID};
                    service.getDetails(request, function (place, status) {
                        if (status === google.maps.places.PlacesServiceStatus.OK) {
                            mapa.origenLatLng = new google.maps.LatLng(place.geometry.location.lat(), place.geometry.location.lng());
                        }
                    });

                    request = {placeId: mapa.destinoPlaceID};
                    service.getDetails(request, function (place, status) {
                        if (status === google.maps.places.PlacesServiceStatus.OK) {
                            mapa.destinoLatlng = new google.maps.LatLng(place.geometry.location.lat(), place.geometry.location.lng());
                        }
                    });

                    imprimir(mapa);
                });

                //detectar parámetros para desplegar ruta
                if (mapa.origenLatLng === null) {
                    mensajear('Se desplegará la ruta con direciones en origen y destino ingresadas por el usuario.');
                    displayRoute(mapa.origen, mapa.destino, directionsService, directionsDisplay, geocoder);
                } else if (mapa.origenDireccion !== getOrigen() && mapa.destinoDireccion === getDestino()) {
                    mensajear('Se desplegará la ruta con el origen ingresado por el usuario y el destino por georeferencia.');
                    mapa.origen = getOrigen();
                    displayRoute(mapa.origen, mapa.destinoLatlng, directionsService, directionsDisplay, geocoder);
                } else if (mapa.origenDireccion === getOrigen() && mapa.destinoDireccion !== getDestino()) {
                    mensajear('Se desplegará la ruta con el origen por georeferencia y el destino ingresado por el usuario.');
                    mapa.destino = getDestino();
                    displayRoute(mapa.origenLatLng, mapa.destino, directionsService, directionsDisplay, geocoder);
                } else if (mapa.origenDireccion !== getOrigen() && mapa.destinoDireccion !== getDestino()) {
                    mensajear('Se desplegará la ruta con direciones en origen y destino ingresadas por el usuario aunque se tienen georeferencias previas.');
                    mapa.origen = getOrigen();
                    mapa.destino = getDestino();
                    displayRoute(mapa.origen, mapa.destino, directionsService, directionsDisplay, geocoder);
                } else {
                    mensajear('Se desplefará la ruta con direcciones por georeferencia.');
                    displayRoute(mapa.origenLatLng, mapa.destinoLatlng, directionsService, directionsDisplay, geocoder);
                }
            }

            function displayRoute(origin, destination, service, display, geocoder) {
//                console.log(origin);
//                console.log(destination);
                service.route({
                    origin: origin,
                    destination: destination,
                    travelMode: 'DRIVING',
                    avoidTolls: true
                }, function (response, status) {
                    if (status === 'OK') {
                        display.setDirections(response);
//                        console.log(response);
                        mapa.origenPlaceID = response.geocoded_waypoints[0].place_id;
                        mapa.destinoPlaceID = response.geocoded_waypoints[1].place_id;
                        geocodePlaceId(geocoder);
                    } else {
                        mensajear('No se puede mostrar la dirección debido a: ' + status + '.');
                    }
                });
            }

            function computeTotalDistance(result) {
                var total = 0;
                var myroute = result.routes[0];
                for (var i = 0; i < myroute.legs.length; i++) {
                    total += myroute.legs[i].distance.value;
                }
                total = total / 1000;
                setDistancia(total);
                mensajear('La distancia calculada fue:' + total + ' Km.');
//                console.log();
            }

            function geocodePlaceId(geocoder) {
                geocoder.geocode({'placeId': mapa.origenPlaceID}, function (results, status) {
                    if (status === 'OK') {
                        if (results[0]) {
                            mapa.origenDireccion = results[0].formatted_address;
                            if (cambio === cambioOrigen) {
                                setOrigen(mapa.origenDireccion);
                            }
//                            window.parent.actualizarDistancia(total);
                        } else {
                            mensajear('El geocodificador no encontró resultados para el origen.');
                        }
                    } else {
                        mensajear('El geocoficador ha fallado para el origen debido a: ' + status);
                    }
                });
                geocoder.geocode({'placeId': mapa.destinoPlaceID}, function (results, status) {
                    if (status === 'OK') {
                        if (results[0]) {
                            mapa.destinoDireccion = results[0].formatted_address;
                            if (cambio === cambioDestino) {
                                setDestino(mapa.destinoDireccion);
                            }
//                            window.parent.actualizarDistancia(total);
                        } else {
                            mensajear('El geocodificador no encontró resultados para el destino.');
                        }
                    } else {
                        mensajear('El geocoficador ha fallado para el destino debido a: ' + status + '.');
                    }
                });
            }

            function imprimir(obj) {
//                console.log(obj);
//                console.log('Cambio: ' + cambio);
            }

            function mensajear(mensaje) {
                console.log(mensaje);
                window.parent.mensajear(mensaje);
            }

            function getOrigen() {
                return window.parent.getOrigen(); //'<%= origen%>'; //document.getElementById('origen').value;
            }

            function setOrigen(direccion) {
                window.parent.actualizarOrigen(direccion);
            }

            function getDestino() {
                return window.parent.getDestino(); // '<%= destino%>'; //document.getElementById('destino').value;
            }

            function setDestino(direccion) {
                window.parent.actualizarDestino(direccion);
            }

            function setDistancia(distancia) {
                mapa.distancia = distancia;
                window.parent.actualizarDistancia(distancia);
            }

            function getInputDestino() {
                return window.parent.document.getElementById('destino');
            }
        </script>
        <script async defer
                src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCxklCLiP2h1mqJsyliNYcz1WmILFtFL-Y&libraries=places&callback=initMap">
        </script>
    </body>
</html>