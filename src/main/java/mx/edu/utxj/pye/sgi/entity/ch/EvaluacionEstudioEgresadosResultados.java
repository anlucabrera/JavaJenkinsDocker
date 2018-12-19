/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "evaluacion_estudio_egresados_resultados", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findAll", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByEvaluacion", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.evaluacionEstudioEgresadosResultadosPK.evaluacion = :evaluacion")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByEvaluador", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.evaluacionEstudioEgresadosResultadosPK.evaluador = :evaluador")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR1", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r1 = :r1")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR2", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r2 = :r2")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR3", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r3 = :r3")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR4", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r4 = :r4")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR5", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r5 = :r5")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR6", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r6 = :r6")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR7", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r7 = :r7")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR8", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r8 = :r8")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR9", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r9 = :r9")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR10", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r10 = :r10")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR11", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r11 = :r11")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR12", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r12 = :r12")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR13", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r13 = :r13")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR14", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r14 = :r14")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR15", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r15 = :r15")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR16", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r16 = :r16")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR17", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r17 = :r17")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR18", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r18 = :r18")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR19", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r19 = :r19")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR20", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r20 = :r20")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR21", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r21 = :r21")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR22", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r22 = :r22")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR23", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r23 = :r23")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR24", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r24 = :r24")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR25", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r25 = :r25")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR26", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r26 = :r26")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR27", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r27 = :r27")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR28", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r28 = :r28")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR29", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r29 = :r29")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR30", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r30 = :r30")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR31", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r31 = :r31")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR32", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r32 = :r32")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR33", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r33 = :r33")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR34", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r34 = :r34")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR35", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r35 = :r35")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR36", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r36 = :r36")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR37", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r37 = :r37")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR38", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r38 = :r38")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR39", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r39 = :r39")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR40", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r40 = :r40")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR41", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r41 = :r41")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR42", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r42 = :r42")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR43", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r43 = :r43")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR44", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r44 = :r44")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR45", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r45 = :r45")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR46", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r46 = :r46")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR47", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r47 = :r47")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR48", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r48 = :r48")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR49", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r49 = :r49")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR50", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r50 = :r50")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR51", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r51 = :r51")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR52", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r52 = :r52")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR53", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r53 = :r53")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR54", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r54 = :r54")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR55", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r55 = :r55")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR56", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r56 = :r56")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR57", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r57 = :r57")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR58", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r58 = :r58")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR59", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r59 = :r59")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR60", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r60 = :r60")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR61", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r61 = :r61")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR62", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r62 = :r62")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR63", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r63 = :r63")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR64a", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r64a = :r64a")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR64b", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r64b = :r64b")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR65", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r65 = :r65")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR66", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r66 = :r66")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR67a", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r67a = :r67a")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR67b", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r67b = :r67b")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR67c", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r67c = :r67c")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR67d", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r67d = :r67d")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR67e", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r67e = :r67e")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR67f", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r67f = :r67f")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR68", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r68 = :r68")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR69", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r69 = :r69")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR70", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r70 = :r70")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR71", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r71 = :r71")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR72", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r72 = :r72")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR73", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r73 = :r73")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR74", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r74 = :r74")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR75", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r75 = :r75")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR76", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r76 = :r76")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR77", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r77 = :r77")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR78", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r78 = :r78")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR79", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r79 = :r79")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR80", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r80 = :r80")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR81", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r81 = :r81")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR82", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r82 = :r82")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR83", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r83 = :r83")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR84", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r84 = :r84")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR85", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r85 = :r85")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR86a", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r86a = :r86a")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR86b", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r86b = :r86b")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR86c", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r86c = :r86c")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR86d", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r86d = :r86d")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR86e", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r86e = :r86e")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR86f", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r86f = :r86f")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR86g", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r86g = :r86g")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR86h", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r86h = :r86h")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR86i", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r86i = :r86i")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR86j", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r86j = :r86j")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR86k", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r86k = :r86k")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR86l", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r86l = :r86l")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR87", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r87 = :r87")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR88", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r88 = :r88")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR89", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r89 = :r89")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR90aa", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r90aa = :r90aa")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR90ab", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r90ab = :r90ab")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR90ba", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r90ba = :r90ba")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR90bb", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r90bb = :r90bb")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR90ca", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r90ca = :r90ca")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR90cb", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r90cb = :r90cb")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR90da", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r90da = :r90da")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR90db", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r90db = :r90db")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR90ea", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r90ea = :r90ea")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR90eb", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r90eb = :r90eb")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR90fa", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r90fa = :r90fa")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR90fb", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r90fb = :r90fb")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR90ga", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r90ga = :r90ga")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR90gb", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r90gb = :r90gb")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR90ha", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r90ha = :r90ha")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR90hb", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r90hb = :r90hb")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR90ia", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r90ia = :r90ia")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR90ib", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r90ib = :r90ib")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR90ja", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r90ja = :r90ja")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR90jb", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r90jb = :r90jb")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR90ka", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r90ka = :r90ka")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR90kb", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r90kb = :r90kb")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR915a", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r915a = :r915a")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR915b", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r915b = :r915b")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR915c", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r915c = :r915c")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR915d", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r915d = :r915d")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR915e", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r915e = :r915e")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR915f", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r915f = :r915f")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR915g", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r915g = :r915g")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR915h", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r915h = :r915h")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR915i", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r915i = :r915i")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR915j", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r915j = :r915j")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR91a", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r91a = :r91a")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR91b", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r91b = :r91b")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR91c", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r91c = :r91c")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR91d", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r91d = :r91d")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR91e", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r91e = :r91e")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR91f", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r91f = :r91f")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR91g", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r91g = :r91g")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR91h", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r91h = :r91h")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR91i", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r91i = :r91i")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR91j", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r91j = :r91j")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR91k", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r91k = :r91k")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR91l", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r91l = :r91l")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR91m", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r91m = :r91m")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR92", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r92 = :r92")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR93", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r93 = :r93")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR94", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r94 = :r94")
    , @NamedQuery(name = "EvaluacionEstudioEgresadosResultados.findByR95", query = "SELECT e FROM EvaluacionEstudioEgresadosResultados e WHERE e.r95 = :r95")})
public class EvaluacionEstudioEgresadosResultados implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EvaluacionEstudioEgresadosResultadosPK evaluacionEstudioEgresadosResultadosPK;
    @Size(max = 255)
    @Column(name = "r1")
    private String r1;
    @Size(max = 50)
    @Column(name = "r2")
    private String r2;
    @Size(max = 50)
    @Column(name = "r3")
    private String r3;
    @Size(max = 255)
    @Column(name = "r4")
    private String r4;
    @Column(name = "r5")
    private Short r5;
    @Column(name = "r6")
    private Short r6;
    @Column(name = "r7")
    private Short r7;
    @Column(name = "r8")
    private Short r8;
    @Size(max = 255)
    @Column(name = "r9")
    private String r9;
    @Size(max = 10)
    @Column(name = "r10")
    private String r10;
    @Size(max = 10)
    @Column(name = "r11")
    private String r11;
    @Size(max = 10)
    @Column(name = "r12")
    private String r12;
    @Size(max = 10)
    @Column(name = "r13")
    private String r13;
    @Size(max = 255)
    @Column(name = "r14")
    private String r14;
    @Size(max = 255)
    @Column(name = "r15")
    private String r15;
    @Size(max = 255)
    @Column(name = "r16")
    private String r16;
    @Size(max = 255)
    @Column(name = "r17")
    private String r17;
    @Size(max = 255)
    @Column(name = "r18")
    private String r18;
    @Size(max = 255)
    @Column(name = "r19")
    private String r19;
    @Size(max = 255)
    @Column(name = "r20")
    private String r20;
    @Size(max = 255)
    @Column(name = "r21")
    private String r21;
    @Size(max = 255)
    @Column(name = "r22")
    private String r22;
    @Size(max = 255)
    @Column(name = "r23")
    private String r23;
    @Size(max = 255)
    @Column(name = "r24")
    private String r24;
    @Size(max = 255)
    @Column(name = "r25")
    private String r25;
    @Size(max = 255)
    @Column(name = "r26")
    private String r26;
    @Size(max = 255)
    @Column(name = "r27")
    private String r27;
    @Size(max = 255)
    @Column(name = "r28")
    private String r28;
    @Size(max = 255)
    @Column(name = "r29")
    private String r29;
    @Size(max = 255)
    @Column(name = "r30")
    private String r30;
    @Size(max = 255)
    @Column(name = "r31")
    private String r31;
    @Size(max = 255)
    @Column(name = "r32")
    private String r32;
    @Size(max = 255)
    @Column(name = "r33")
    private String r33;
    @Size(max = 255)
    @Column(name = "r34")
    private String r34;
    @Size(max = 255)
    @Column(name = "r35")
    private String r35;
    @Size(max = 255)
    @Column(name = "r36")
    private String r36;
    @Size(max = 255)
    @Column(name = "r37")
    private String r37;
    @Size(max = 255)
    @Column(name = "r38")
    private String r38;
    @Size(max = 255)
    @Column(name = "r39")
    private String r39;
    @Size(max = 255)
    @Column(name = "r40")
    private String r40;
    @Size(max = 255)
    @Column(name = "r41")
    private String r41;
    @Size(max = 255)
    @Column(name = "r42")
    private String r42;
    @Size(max = 10)
    @Column(name = "r43")
    private String r43;
    @Size(max = 255)
    @Column(name = "r44")
    private String r44;
    @Size(max = 255)
    @Column(name = "r45")
    private String r45;
    @Size(max = 10)
    @Column(name = "r46")
    private String r46;
    @Size(max = 255)
    @Column(name = "r47")
    private String r47;
    @Size(max = 255)
    @Column(name = "r48")
    private String r48;
    @Size(max = 10)
    @Column(name = "r49")
    private String r49;
    @Size(max = 255)
    @Column(name = "r50")
    private String r50;
    @Size(max = 255)
    @Column(name = "r51")
    private String r51;
    @Size(max = 10)
    @Column(name = "r52")
    private String r52;
    @Size(max = 255)
    @Column(name = "r53")
    private String r53;
    @Size(max = 255)
    @Column(name = "r54")
    private String r54;
    @Size(max = 10)
    @Column(name = "r55")
    private String r55;
    @Size(max = 255)
    @Column(name = "r56")
    private String r56;
    @Size(max = 255)
    @Column(name = "r57")
    private String r57;
    @Size(max = 10)
    @Column(name = "r58")
    private String r58;
    @Size(max = 255)
    @Column(name = "r59")
    private String r59;
    @Column(name = "r60")
    private Short r60;
    @Column(name = "r61")
    private Short r61;
    @Column(name = "r62")
    private Short r62;
    @Column(name = "r63")
    private Short r63;
    @Column(name = "r64a")
    private Short r64a;
    @Column(name = "r64b")
    private Integer r64b;
    @Column(name = "r65")
    private Short r65;
    @Column(name = "r66")
    private Short r66;
    @Column(name = "r67a")
    private Short r67a;
    @Column(name = "r67b")
    private Short r67b;
    @Column(name = "r67c")
    private Short r67c;
    @Column(name = "r67d")
    private Short r67d;
    @Column(name = "r67e")
    private Short r67e;
    @Column(name = "r67f")
    private Short r67f;
    @Size(max = 255)
    @Column(name = "r68")
    private String r68;
    @Size(max = 255)
    @Column(name = "r69")
    private String r69;
    @Size(max = 255)
    @Column(name = "r70")
    private String r70;
    @Size(max = 255)
    @Column(name = "r71")
    private String r71;
    @Size(max = 255)
    @Column(name = "r72")
    private String r72;
    @Size(max = 255)
    @Column(name = "r73")
    private String r73;
    @Size(max = 255)
    @Column(name = "r74")
    private String r74;
    @Size(max = 10)
    @Column(name = "r75")
    private String r75;
    @Size(max = 10)
    @Column(name = "r76")
    private String r76;
    @Size(max = 255)
    @Column(name = "r77")
    private String r77;
    @Column(name = "r78")
    private Short r78;
    @Column(name = "r79")
    private Short r79;
    @Column(name = "r80")
    private Short r80;
    @Column(name = "r81")
    private Short r81;
    @Column(name = "r82")
    private Short r82;
    @Column(name = "r83")
    private Short r83;
    @Column(name = "r84")
    private Short r84;
    @Column(name = "r85")
    private Short r85;
    @Column(name = "r86a")
    private Short r86a;
    @Column(name = "r86b")
    private Short r86b;
    @Column(name = "r86c")
    private Short r86c;
    @Column(name = "r86d")
    private Short r86d;
    @Column(name = "r86e")
    private Short r86e;
    @Column(name = "r86f")
    private Short r86f;
    @Column(name = "r86g")
    private Short r86g;
    @Column(name = "r86h")
    private Short r86h;
    @Column(name = "r86i")
    private Short r86i;
    @Column(name = "r86j")
    private Short r86j;
    @Column(name = "r86k")
    private Short r86k;
    @Column(name = "r86l")
    private Short r86l;
    @Column(name = "r87")
    private Short r87;
    @Column(name = "r88")
    private Short r88;
    @Column(name = "r89")
    private Short r89;
    @Column(name = "r90aa")
    private Short r90aa;
    @Column(name = "r90ab")
    private Short r90ab;
    @Column(name = "r90ba")
    private Short r90ba;
    @Column(name = "r90bb")
    private Short r90bb;
    @Column(name = "r90ca")
    private Short r90ca;
    @Column(name = "r90cb")
    private Short r90cb;
    @Column(name = "r90da")
    private Short r90da;
    @Column(name = "r90db")
    private Short r90db;
    @Column(name = "r90ea")
    private Short r90ea;
    @Column(name = "r90eb")
    private Short r90eb;
    @Column(name = "r90fa")
    private Short r90fa;
    @Column(name = "r90fb")
    private Short r90fb;
    @Column(name = "r90ga")
    private Short r90ga;
    @Column(name = "r90gb")
    private Short r90gb;
    @Column(name = "r90ha")
    private Short r90ha;
    @Column(name = "r90hb")
    private Short r90hb;
    @Column(name = "r90ia")
    private Short r90ia;
    @Column(name = "r90ib")
    private Short r90ib;
    @Column(name = "r90ja")
    private Short r90ja;
    @Column(name = "r90jb")
    private Short r90jb;
    @Column(name = "r90ka")
    private Short r90ka;
    @Column(name = "r90kb")
    private Short r90kb;
    @Column(name = "r915a")
    private Short r915a;
    @Column(name = "r915b")
    private Short r915b;
    @Column(name = "r915c")
    private Short r915c;
    @Column(name = "r915d")
    private Short r915d;
    @Column(name = "r915e")
    private Short r915e;
    @Column(name = "r915f")
    private Short r915f;
    @Column(name = "r915g")
    private Short r915g;
    @Column(name = "r915h")
    private Short r915h;
    @Column(name = "r915i")
    private Short r915i;
    @Column(name = "r915j")
    private Short r915j;
    @Column(name = "r91a")
    private Short r91a;
    @Column(name = "r91b")
    private Short r91b;
    @Column(name = "r91c")
    private Short r91c;
    @Column(name = "r91d")
    private Short r91d;
    @Column(name = "r91e")
    private Short r91e;
    @Column(name = "r91f")
    private Short r91f;
    @Column(name = "r91g")
    private Short r91g;
    @Column(name = "r91h")
    private Short r91h;
    @Column(name = "r91i")
    private Short r91i;
    @Column(name = "r91j")
    private Short r91j;
    @Column(name = "r91k")
    private Short r91k;
    @Column(name = "r91l")
    private Short r91l;
    @Column(name = "r91m")
    private Short r91m;
    @Column(name = "r92")
    private Short r92;
    @Column(name = "r93")
    private Short r93;
    @Size(max = 1500)
    @Column(name = "r94")
    private String r94;
    @Size(max = 1500)
    @Column(name = "r95")
    private String r95;

    public EvaluacionEstudioEgresadosResultados() {
    }

    public EvaluacionEstudioEgresadosResultados(EvaluacionEstudioEgresadosResultadosPK evaluacionEstudioEgresadosResultadosPK) {
        this.evaluacionEstudioEgresadosResultadosPK = evaluacionEstudioEgresadosResultadosPK;
    }

    public EvaluacionEstudioEgresadosResultados(int evaluacion, int evaluador) {
        this.evaluacionEstudioEgresadosResultadosPK = new EvaluacionEstudioEgresadosResultadosPK(evaluacion, evaluador);
    }

    public EvaluacionEstudioEgresadosResultadosPK getEvaluacionEstudioEgresadosResultadosPK() {
        return evaluacionEstudioEgresadosResultadosPK;
    }

    public void setEvaluacionEstudioEgresadosResultadosPK(EvaluacionEstudioEgresadosResultadosPK evaluacionEstudioEgresadosResultadosPK) {
        this.evaluacionEstudioEgresadosResultadosPK = evaluacionEstudioEgresadosResultadosPK;
    }

    public String getR1() {
        return r1;
    }

    public void setR1(String r1) {
        this.r1 = r1;
    }

    public String getR2() {
        return r2;
    }

    public void setR2(String r2) {
        this.r2 = r2;
    }

    public String getR3() {
        return r3;
    }

    public void setR3(String r3) {
        this.r3 = r3;
    }

    public String getR4() {
        return r4;
    }

    public void setR4(String r4) {
        this.r4 = r4;
    }

    public Short getR5() {
        return r5;
    }

    public void setR5(Short r5) {
        this.r5 = r5;
    }

    public Short getR6() {
        return r6;
    }

    public void setR6(Short r6) {
        this.r6 = r6;
    }

    public Short getR7() {
        return r7;
    }

    public void setR7(Short r7) {
        this.r7 = r7;
    }

    public Short getR8() {
        return r8;
    }

    public void setR8(Short r8) {
        this.r8 = r8;
    }

    public String getR9() {
        return r9;
    }

    public void setR9(String r9) {
        this.r9 = r9;
    }

    public String getR10() {
        return r10;
    }

    public void setR10(String r10) {
        this.r10 = r10;
    }

    public String getR11() {
        return r11;
    }

    public void setR11(String r11) {
        this.r11 = r11;
    }

    public String getR12() {
        return r12;
    }

    public void setR12(String r12) {
        this.r12 = r12;
    }

    public String getR13() {
        return r13;
    }

    public void setR13(String r13) {
        this.r13 = r13;
    }

    public String getR14() {
        return r14;
    }

    public void setR14(String r14) {
        this.r14 = r14;
    }

    public String getR15() {
        return r15;
    }

    public void setR15(String r15) {
        this.r15 = r15;
    }

    public String getR16() {
        return r16;
    }

    public void setR16(String r16) {
        this.r16 = r16;
    }

    public String getR17() {
        return r17;
    }

    public void setR17(String r17) {
        this.r17 = r17;
    }

    public String getR18() {
        return r18;
    }

    public void setR18(String r18) {
        this.r18 = r18;
    }

    public String getR19() {
        return r19;
    }

    public void setR19(String r19) {
        this.r19 = r19;
    }

    public String getR20() {
        return r20;
    }

    public void setR20(String r20) {
        this.r20 = r20;
    }

    public String getR21() {
        return r21;
    }

    public void setR21(String r21) {
        this.r21 = r21;
    }

    public String getR22() {
        return r22;
    }

    public void setR22(String r22) {
        this.r22 = r22;
    }

    public String getR23() {
        return r23;
    }

    public void setR23(String r23) {
        this.r23 = r23;
    }

    public String getR24() {
        return r24;
    }

    public void setR24(String r24) {
        this.r24 = r24;
    }

    public String getR25() {
        return r25;
    }

    public void setR25(String r25) {
        this.r25 = r25;
    }

    public String getR26() {
        return r26;
    }

    public void setR26(String r26) {
        this.r26 = r26;
    }

    public String getR27() {
        return r27;
    }

    public void setR27(String r27) {
        this.r27 = r27;
    }

    public String getR28() {
        return r28;
    }

    public void setR28(String r28) {
        this.r28 = r28;
    }

    public String getR29() {
        return r29;
    }

    public void setR29(String r29) {
        this.r29 = r29;
    }

    public String getR30() {
        return r30;
    }

    public void setR30(String r30) {
        this.r30 = r30;
    }

    public String getR31() {
        return r31;
    }

    public void setR31(String r31) {
        this.r31 = r31;
    }

    public String getR32() {
        return r32;
    }

    public void setR32(String r32) {
        this.r32 = r32;
    }

    public String getR33() {
        return r33;
    }

    public void setR33(String r33) {
        this.r33 = r33;
    }

    public String getR34() {
        return r34;
    }

    public void setR34(String r34) {
        this.r34 = r34;
    }

    public String getR35() {
        return r35;
    }

    public void setR35(String r35) {
        this.r35 = r35;
    }

    public String getR36() {
        return r36;
    }

    public void setR36(String r36) {
        this.r36 = r36;
    }

    public String getR37() {
        return r37;
    }

    public void setR37(String r37) {
        this.r37 = r37;
    }

    public String getR38() {
        return r38;
    }

    public void setR38(String r38) {
        this.r38 = r38;
    }

    public String getR39() {
        return r39;
    }

    public void setR39(String r39) {
        this.r39 = r39;
    }

    public String getR40() {
        return r40;
    }

    public void setR40(String r40) {
        this.r40 = r40;
    }

    public String getR41() {
        return r41;
    }

    public void setR41(String r41) {
        this.r41 = r41;
    }

    public String getR42() {
        return r42;
    }

    public void setR42(String r42) {
        this.r42 = r42;
    }

    public String getR43() {
        return r43;
    }

    public void setR43(String r43) {
        this.r43 = r43;
    }

    public String getR44() {
        return r44;
    }

    public void setR44(String r44) {
        this.r44 = r44;
    }

    public String getR45() {
        return r45;
    }

    public void setR45(String r45) {
        this.r45 = r45;
    }

    public String getR46() {
        return r46;
    }

    public void setR46(String r46) {
        this.r46 = r46;
    }

    public String getR47() {
        return r47;
    }

    public void setR47(String r47) {
        this.r47 = r47;
    }

    public String getR48() {
        return r48;
    }

    public void setR48(String r48) {
        this.r48 = r48;
    }

    public String getR49() {
        return r49;
    }

    public void setR49(String r49) {
        this.r49 = r49;
    }

    public String getR50() {
        return r50;
    }

    public void setR50(String r50) {
        this.r50 = r50;
    }

    public String getR51() {
        return r51;
    }

    public void setR51(String r51) {
        this.r51 = r51;
    }

    public String getR52() {
        return r52;
    }

    public void setR52(String r52) {
        this.r52 = r52;
    }

    public String getR53() {
        return r53;
    }

    public void setR53(String r53) {
        this.r53 = r53;
    }

    public String getR54() {
        return r54;
    }

    public void setR54(String r54) {
        this.r54 = r54;
    }

    public String getR55() {
        return r55;
    }

    public void setR55(String r55) {
        this.r55 = r55;
    }

    public String getR56() {
        return r56;
    }

    public void setR56(String r56) {
        this.r56 = r56;
    }

    public String getR57() {
        return r57;
    }

    public void setR57(String r57) {
        this.r57 = r57;
    }

    public String getR58() {
        return r58;
    }

    public void setR58(String r58) {
        this.r58 = r58;
    }

    public String getR59() {
        return r59;
    }

    public void setR59(String r59) {
        this.r59 = r59;
    }

    public Short getR60() {
        return r60;
    }

    public void setR60(Short r60) {
        this.r60 = r60;
    }

    public Short getR61() {
        return r61;
    }

    public void setR61(Short r61) {
        this.r61 = r61;
    }

    public Short getR62() {
        return r62;
    }

    public void setR62(Short r62) {
        this.r62 = r62;
    }

    public Short getR63() {
        return r63;
    }

    public void setR63(Short r63) {
        this.r63 = r63;
    }

    public Short getR64a() {
        return r64a;
    }

    public void setR64a(Short r64a) {
        this.r64a = r64a;
    }

    public Integer getR64b() {
        return r64b;
    }

    public void setR64b(Integer r64b) {
        this.r64b = r64b;
    }

    public Short getR65() {
        return r65;
    }

    public void setR65(Short r65) {
        this.r65 = r65;
    }

    public Short getR66() {
        return r66;
    }

    public void setR66(Short r66) {
        this.r66 = r66;
    }

    public Short getR67a() {
        return r67a;
    }

    public void setR67a(Short r67a) {
        this.r67a = r67a;
    }

    public Short getR67b() {
        return r67b;
    }

    public void setR67b(Short r67b) {
        this.r67b = r67b;
    }

    public Short getR67c() {
        return r67c;
    }

    public void setR67c(Short r67c) {
        this.r67c = r67c;
    }

    public Short getR67d() {
        return r67d;
    }

    public void setR67d(Short r67d) {
        this.r67d = r67d;
    }

    public Short getR67e() {
        return r67e;
    }

    public void setR67e(Short r67e) {
        this.r67e = r67e;
    }

    public Short getR67f() {
        return r67f;
    }

    public void setR67f(Short r67f) {
        this.r67f = r67f;
    }

    public String getR68() {
        return r68;
    }

    public void setR68(String r68) {
        this.r68 = r68;
    }

    public String getR69() {
        return r69;
    }

    public void setR69(String r69) {
        this.r69 = r69;
    }

    public String getR70() {
        return r70;
    }

    public void setR70(String r70) {
        this.r70 = r70;
    }

    public String getR71() {
        return r71;
    }

    public void setR71(String r71) {
        this.r71 = r71;
    }

    public String getR72() {
        return r72;
    }

    public void setR72(String r72) {
        this.r72 = r72;
    }

    public String getR73() {
        return r73;
    }

    public void setR73(String r73) {
        this.r73 = r73;
    }

    public String getR74() {
        return r74;
    }

    public void setR74(String r74) {
        this.r74 = r74;
    }

    public String getR75() {
        return r75;
    }

    public void setR75(String r75) {
        this.r75 = r75;
    }

    public String getR76() {
        return r76;
    }

    public void setR76(String r76) {
        this.r76 = r76;
    }

    public String getR77() {
        return r77;
    }

    public void setR77(String r77) {
        this.r77 = r77;
    }

    public Short getR78() {
        return r78;
    }

    public void setR78(Short r78) {
        this.r78 = r78;
    }

    public Short getR79() {
        return r79;
    }

    public void setR79(Short r79) {
        this.r79 = r79;
    }

    public Short getR80() {
        return r80;
    }

    public void setR80(Short r80) {
        this.r80 = r80;
    }

    public Short getR81() {
        return r81;
    }

    public void setR81(Short r81) {
        this.r81 = r81;
    }

    public Short getR82() {
        return r82;
    }

    public void setR82(Short r82) {
        this.r82 = r82;
    }

    public Short getR83() {
        return r83;
    }

    public void setR83(Short r83) {
        this.r83 = r83;
    }

    public Short getR84() {
        return r84;
    }

    public void setR84(Short r84) {
        this.r84 = r84;
    }

    public Short getR85() {
        return r85;
    }

    public void setR85(Short r85) {
        this.r85 = r85;
    }

    public Short getR86a() {
        return r86a;
    }

    public void setR86a(Short r86a) {
        this.r86a = r86a;
    }

    public Short getR86b() {
        return r86b;
    }

    public void setR86b(Short r86b) {
        this.r86b = r86b;
    }

    public Short getR86c() {
        return r86c;
    }

    public void setR86c(Short r86c) {
        this.r86c = r86c;
    }

    public Short getR86d() {
        return r86d;
    }

    public void setR86d(Short r86d) {
        this.r86d = r86d;
    }

    public Short getR86e() {
        return r86e;
    }

    public void setR86e(Short r86e) {
        this.r86e = r86e;
    }

    public Short getR86f() {
        return r86f;
    }

    public void setR86f(Short r86f) {
        this.r86f = r86f;
    }

    public Short getR86g() {
        return r86g;
    }

    public void setR86g(Short r86g) {
        this.r86g = r86g;
    }

    public Short getR86h() {
        return r86h;
    }

    public void setR86h(Short r86h) {
        this.r86h = r86h;
    }

    public Short getR86i() {
        return r86i;
    }

    public void setR86i(Short r86i) {
        this.r86i = r86i;
    }

    public Short getR86j() {
        return r86j;
    }

    public void setR86j(Short r86j) {
        this.r86j = r86j;
    }

    public Short getR86k() {
        return r86k;
    }

    public void setR86k(Short r86k) {
        this.r86k = r86k;
    }

    public Short getR86l() {
        return r86l;
    }

    public void setR86l(Short r86l) {
        this.r86l = r86l;
    }

    public Short getR87() {
        return r87;
    }

    public void setR87(Short r87) {
        this.r87 = r87;
    }

    public Short getR88() {
        return r88;
    }

    public void setR88(Short r88) {
        this.r88 = r88;
    }

    public Short getR89() {
        return r89;
    }

    public void setR89(Short r89) {
        this.r89 = r89;
    }

    public Short getR90aa() {
        return r90aa;
    }

    public void setR90aa(Short r90aa) {
        this.r90aa = r90aa;
    }

    public Short getR90ab() {
        return r90ab;
    }

    public void setR90ab(Short r90ab) {
        this.r90ab = r90ab;
    }

    public Short getR90ba() {
        return r90ba;
    }

    public void setR90ba(Short r90ba) {
        this.r90ba = r90ba;
    }

    public Short getR90bb() {
        return r90bb;
    }

    public void setR90bb(Short r90bb) {
        this.r90bb = r90bb;
    }

    public Short getR90ca() {
        return r90ca;
    }

    public void setR90ca(Short r90ca) {
        this.r90ca = r90ca;
    }

    public Short getR90cb() {
        return r90cb;
    }

    public void setR90cb(Short r90cb) {
        this.r90cb = r90cb;
    }

    public Short getR90da() {
        return r90da;
    }

    public void setR90da(Short r90da) {
        this.r90da = r90da;
    }

    public Short getR90db() {
        return r90db;
    }

    public void setR90db(Short r90db) {
        this.r90db = r90db;
    }

    public Short getR90ea() {
        return r90ea;
    }

    public void setR90ea(Short r90ea) {
        this.r90ea = r90ea;
    }

    public Short getR90eb() {
        return r90eb;
    }

    public void setR90eb(Short r90eb) {
        this.r90eb = r90eb;
    }

    public Short getR90fa() {
        return r90fa;
    }

    public void setR90fa(Short r90fa) {
        this.r90fa = r90fa;
    }

    public Short getR90fb() {
        return r90fb;
    }

    public void setR90fb(Short r90fb) {
        this.r90fb = r90fb;
    }

    public Short getR90ga() {
        return r90ga;
    }

    public void setR90ga(Short r90ga) {
        this.r90ga = r90ga;
    }

    public Short getR90gb() {
        return r90gb;
    }

    public void setR90gb(Short r90gb) {
        this.r90gb = r90gb;
    }

    public Short getR90ha() {
        return r90ha;
    }

    public void setR90ha(Short r90ha) {
        this.r90ha = r90ha;
    }

    public Short getR90hb() {
        return r90hb;
    }

    public void setR90hb(Short r90hb) {
        this.r90hb = r90hb;
    }

    public Short getR90ia() {
        return r90ia;
    }

    public void setR90ia(Short r90ia) {
        this.r90ia = r90ia;
    }

    public Short getR90ib() {
        return r90ib;
    }

    public void setR90ib(Short r90ib) {
        this.r90ib = r90ib;
    }

    public Short getR90ja() {
        return r90ja;
    }

    public void setR90ja(Short r90ja) {
        this.r90ja = r90ja;
    }

    public Short getR90jb() {
        return r90jb;
    }

    public void setR90jb(Short r90jb) {
        this.r90jb = r90jb;
    }

    public Short getR90ka() {
        return r90ka;
    }

    public void setR90ka(Short r90ka) {
        this.r90ka = r90ka;
    }

    public Short getR90kb() {
        return r90kb;
    }

    public void setR90kb(Short r90kb) {
        this.r90kb = r90kb;
    }

    public Short getR915a() {
        return r915a;
    }

    public void setR915a(Short r915a) {
        this.r915a = r915a;
    }

    public Short getR915b() {
        return r915b;
    }

    public void setR915b(Short r915b) {
        this.r915b = r915b;
    }

    public Short getR915c() {
        return r915c;
    }

    public void setR915c(Short r915c) {
        this.r915c = r915c;
    }

    public Short getR915d() {
        return r915d;
    }

    public void setR915d(Short r915d) {
        this.r915d = r915d;
    }

    public Short getR915e() {
        return r915e;
    }

    public void setR915e(Short r915e) {
        this.r915e = r915e;
    }

    public Short getR915f() {
        return r915f;
    }

    public void setR915f(Short r915f) {
        this.r915f = r915f;
    }

    public Short getR915g() {
        return r915g;
    }

    public void setR915g(Short r915g) {
        this.r915g = r915g;
    }

    public Short getR915h() {
        return r915h;
    }

    public void setR915h(Short r915h) {
        this.r915h = r915h;
    }

    public Short getR915i() {
        return r915i;
    }

    public void setR915i(Short r915i) {
        this.r915i = r915i;
    }

    public Short getR915j() {
        return r915j;
    }

    public void setR915j(Short r915j) {
        this.r915j = r915j;
    }

    public Short getR91a() {
        return r91a;
    }

    public void setR91a(Short r91a) {
        this.r91a = r91a;
    }

    public Short getR91b() {
        return r91b;
    }

    public void setR91b(Short r91b) {
        this.r91b = r91b;
    }

    public Short getR91c() {
        return r91c;
    }

    public void setR91c(Short r91c) {
        this.r91c = r91c;
    }

    public Short getR91d() {
        return r91d;
    }

    public void setR91d(Short r91d) {
        this.r91d = r91d;
    }

    public Short getR91e() {
        return r91e;
    }

    public void setR91e(Short r91e) {
        this.r91e = r91e;
    }

    public Short getR91f() {
        return r91f;
    }

    public void setR91f(Short r91f) {
        this.r91f = r91f;
    }

    public Short getR91g() {
        return r91g;
    }

    public void setR91g(Short r91g) {
        this.r91g = r91g;
    }

    public Short getR91h() {
        return r91h;
    }

    public void setR91h(Short r91h) {
        this.r91h = r91h;
    }

    public Short getR91i() {
        return r91i;
    }

    public void setR91i(Short r91i) {
        this.r91i = r91i;
    }

    public Short getR91j() {
        return r91j;
    }

    public void setR91j(Short r91j) {
        this.r91j = r91j;
    }

    public Short getR91k() {
        return r91k;
    }

    public void setR91k(Short r91k) {
        this.r91k = r91k;
    }

    public Short getR91l() {
        return r91l;
    }

    public void setR91l(Short r91l) {
        this.r91l = r91l;
    }

    public Short getR91m() {
        return r91m;
    }

    public void setR91m(Short r91m) {
        this.r91m = r91m;
    }

    public Short getR92() {
        return r92;
    }

    public void setR92(Short r92) {
        this.r92 = r92;
    }

    public Short getR93() {
        return r93;
    }

    public void setR93(Short r93) {
        this.r93 = r93;
    }

    public String getR94() {
        return r94;
    }

    public void setR94(String r94) {
        this.r94 = r94;
    }

    public String getR95() {
        return r95;
    }

    public void setR95(String r95) {
        this.r95 = r95;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (evaluacionEstudioEgresadosResultadosPK != null ? evaluacionEstudioEgresadosResultadosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvaluacionEstudioEgresadosResultados)) {
            return false;
        }
        EvaluacionEstudioEgresadosResultados other = (EvaluacionEstudioEgresadosResultados) object;
        if ((this.evaluacionEstudioEgresadosResultadosPK == null && other.evaluacionEstudioEgresadosResultadosPK != null) || (this.evaluacionEstudioEgresadosResultadosPK != null && !this.evaluacionEstudioEgresadosResultadosPK.equals(other.evaluacionEstudioEgresadosResultadosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.EvaluacionEstudioEgresadosResultados[ evaluacionEstudioEgresadosResultadosPK=" + evaluacionEstudioEgresadosResultadosPK + " ]";
    }
    
}
