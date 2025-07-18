package com.API_partidasFutebol_Meli.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "partidas")
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "clube_mandante_id")
    private Clube clubeMandante;

    @ManyToOne(optional = false)
    @JoinColumn(name = "clube_visitante_id")
    private Clube clubeVisitante;

    @ManyToOne(optional = false)
    @JoinColumn(name = "estadio_id")
    private Estadio estadio;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(name = "gols_mandante", nullable = false)
    private Integer golsMandante;

    @Column(name = "gols_visitante", nullable = false)
    private Integer golsVisitante;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Clube getClubeMandante() {
        return clubeMandante;
    }

    public void setClubeMandante(Clube clubeMandante) {
        this.clubeMandante = clubeMandante;
    }

    public Clube getClubeVisitante() {
        return clubeVisitante;
    }

    public void setClubeVisitante(Clube clubeVisitante) {
        this.clubeVisitante = clubeVisitante;
    }

    public Estadio getEstadio() {
        return estadio;
    }

    public void setEstadio(Estadio estadio) {
        this.estadio = estadio;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Integer getGolsMandante() {
        return golsMandante;
    }

    public void setGolsMandante(Integer golsMandante) {
        this.golsMandante = golsMandante;
    }

    public Integer getGolsVisitante() {
        return golsVisitante;
    }

    public void setGolsVisitante(Integer golsVisitante) {
        this.golsVisitante = golsVisitante;
    }

    public Partida() {}


    public Partida(long l, Clube clube1, Clube clube2, Object o, LocalDateTime localDateTime, int i, int i1) {
        this.id = l;
        this.clubeMandante = clube1;
        this.clubeVisitante = clube2;
        this.estadio = (Estadio) o;
        this.dataHora = localDateTime;
        this.golsMandante = i;
        this.golsVisitante = i1;
    }
}
