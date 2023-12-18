/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ols.doci.process.entity;

/**
 *
 * @author Brandon
 */
public class Detalle {

    private String descripcion;
    private float precioUnitario;
    private float ventasNoSujetas;
    private float ivaItem;
    private String delAl;
    private float exportaciones;
    private String numDocRel;
    private float uniMedidaCodigo;
    private float ventasExentas;
    private String fecha;
    private float tipoItem;
    private String tipoDteRel;
    private String codTributo;
    private float otroMonNoAfec;
    private String codigoRetencionMH;
    private float cantidad;
    private float ventasGravadas;
    private float ivaRetenido;
    private String desc;
    private float descuentoItem;

    public Detalle() {
        this.descripcion = "";
        this.precioUnitario = 0;
        this.ventasNoSujetas = 0;
        this.ivaItem = 0;
        this.delAl = "";
        this.exportaciones = 0;
        this.numDocRel = "";
        this.uniMedidaCodigo = 0;
        this.ventasExentas = 0;
        this.fecha = "";
        this.tipoItem = 0;
        this.tipoDteRel = "";
        this.codTributo = "";
        this.otroMonNoAfec = 0;
        this.codigoRetencionMH = "";
        this.cantidad = 0;
        this.ventasGravadas = 0;
        this.ivaRetenido = 0;
        this.desc = "";
        this.descuentoItem = 0;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(float precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public float getVentasNoSujetas() {
        return ventasNoSujetas;
    }

    public void setVentasNoSujetas(float ventasNoSujetas) {
        this.ventasNoSujetas = ventasNoSujetas;
    }

    public float getIvaItem() {
        return ivaItem;
    }

    public void setIvaItem(float ivaItem) {
        this.ivaItem = ivaItem;
    }

    public String getDelAl() {
        return delAl;
    }

    public void setDelAl(String delAl) {
        this.delAl = delAl;
    }

    public float getExportaciones() {
        return exportaciones;
    }

    public void setExportaciones(float exportaciones) {
        this.exportaciones = exportaciones;
    }

    public String getNumDocRel() {
        return numDocRel;
    }

    public void setNumDocRel(String numDocRel) {
        this.numDocRel = numDocRel;
    }

    public float getUniMedidaCodigo() {
        return uniMedidaCodigo;
    }

    public void setUniMedidaCodigo(float uniMedidaCodigo) {
        this.uniMedidaCodigo = uniMedidaCodigo;
    }

    public float getVentasExentas() {
        return ventasExentas;
    }

    public void setVentasExentas(float ventasExentas) {
        this.ventasExentas = ventasExentas;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public float getTipoItem() {
        return tipoItem;
    }

    public void setTipoItem(float tipoItem) {
        this.tipoItem = tipoItem;
    }

    public String getTipoDteRel() {
        return tipoDteRel;
    }

    public void setTipoDteRel(String tipoDteRel) {
        this.tipoDteRel = tipoDteRel;
    }

    public String getCodTributo() {
        return codTributo;
    }

    public void setCodTributo(String codTributo) {
        this.codTributo = codTributo;
    }

    public float getOtroMonNoAfec() {
        return otroMonNoAfec;
    }

    public void setOtroMonNoAfec(float otroMonNoAfec) {
        this.otroMonNoAfec = otroMonNoAfec;
    }

    public String getCodigoRetencionMH() {
        return codigoRetencionMH;
    }

    public void setCodigoRetencionMH(String codigoRetencionMH) {
        this.codigoRetencionMH = codigoRetencionMH;
    }

    public float getCantidad() {
        return cantidad;
    }

    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }

    public float getVentasGravadas() {
        return ventasGravadas;
    }

    public void setVentasGravadas(float ventasGravadas) {
        this.ventasGravadas = ventasGravadas;
    }

    public float getIvaRetenido() {
        return ivaRetenido;
    }

    public void setIvaRetenido(float ivaRetenido) {
        this.ivaRetenido = ivaRetenido;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public float getDescuentoItem() {
        return descuentoItem;
    }

    public void setDescuentoItem(float descuentoItem) {
        this.descuentoItem = descuentoItem;
    }

}
