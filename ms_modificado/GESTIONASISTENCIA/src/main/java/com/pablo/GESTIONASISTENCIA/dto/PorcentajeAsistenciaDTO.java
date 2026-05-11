package com.pablo.GESTIONASISTENCIA.dto;

public class PorcentajeAsistenciaDTO {

    private Long alumnoId;
    private long totalClases;
    private long clasesPresente;
    private long clasesAtrasado;
    private long clasesAusente;
    private double porcentajeAsistencia;

    public PorcentajeAsistenciaDTO() {
    }

    public PorcentajeAsistenciaDTO(Long alumnoId, long totalClases, long clasesPresente,
                                   long clasesAtrasado, long clasesAusente, double porcentajeAsistencia) {
        this.alumnoId = alumnoId;
        this.totalClases = totalClases;
        this.clasesPresente = clasesPresente;
        this.clasesAtrasado = clasesAtrasado;
        this.clasesAusente = clasesAusente;
        this.porcentajeAsistencia = porcentajeAsistencia;
    }

    public Long getAlumnoId() {
        return alumnoId;
    }

    public void setAlumnoId(Long alumnoId) {
        this.alumnoId = alumnoId;
    }

    public long getTotalClases() {
        return totalClases;
    }

    public void setTotalClases(long totalClases) {
        this.totalClases = totalClases;
    }

    public long getClasesPresente() {
        return clasesPresente;
    }

    public void setClasesPresente(long clasesPresente) {
        this.clasesPresente = clasesPresente;
    }

    public long getClasesAtrasado() {
        return clasesAtrasado;
    }

    public void setClasesAtrasado(long clasesAtrasado) {
        this.clasesAtrasado = clasesAtrasado;
    }

    public long getClasesAusente() {
        return clasesAusente;
    }

    public void setClasesAusente(long clasesAusente) {
        this.clasesAusente = clasesAusente;
    }

    public double getPorcentajeAsistencia() {
        return porcentajeAsistencia;
    }

    public void setPorcentajeAsistencia(double porcentajeAsistencia) {
        this.porcentajeAsistencia = porcentajeAsistencia;
    }
}
