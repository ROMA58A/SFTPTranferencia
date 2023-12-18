/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ols.doci.process.entity;

import java.util.ArrayList;

/**
 *
 * @author Brandon
 */
public class Json {

    public String get_num_doc_receptor() {
        if (this.numDocReceptor != null && !this.numDocReceptor.isBlank()) {
            return this.numDocReceptor;
        }
        if (this.nitCliente != null && !this.nitCliente.isBlank()) {
            return this.nitCliente;
        }
        return this.duiCliente;
    }

    private String direccionEmisor;
    private String responsableReceptor;
    private float seguro;
    private float cantidadTotal;
    private String pdv;
    private String resFin;
    private float fovial;
    private float totalExportaciones;
    private String direccionCliente;
    private String otrosDocIdent;
    private String correlativoInterno;
    private float subTotalVentasGravadas;
    private float ventaTotal;
    private float contribucionSeguridad;
    private String tipoItemExpor;
    private String nit;
    private float ventasGravadas;
    private float cesc;
    private String nrc;
    private String CCFAnterior;
    private String numeroTransaccion;
    private String municipio;
    private float montGDescVentGrav;
    private String nombreComercialCl;
    private String fechaEmision;
    private String ventCterNit;
    private String idMunicipioEmisor;
    private String idDepartamentoReceptor;
    private String codigoActividadEconomica;
    private String tipoDocumento;
    private String fechaEnvio;
    private String duiCliente;
    private float sumas;
    private String numIdenConductor;
    private String vtaACuentaDe;
    private String nomConductor;
    private float renta;
    private String nombrePais;
    private String ventCterNombre;
    private String bienTitulo;
    private String numeroControl;
    private String nombreUsuario;
    private String numDocEmisor;
    private float ventasExentas;
    private String codicionPago;
    private String numControl;
    private String docRelFecha;
    private String nombreCliente;
    private float ivaRetenido13;
    private float ivaRetenido1;
    private String noFecha;
    private float ventasNoGravadas;
    private String montoLetras;
    private float ventasNoSujetas;
    private String codPais;
    private String codGeneracion;
    private String telsucE;
    private String campoExtFE;
    private float montGDescVentNoSujetas;
    private String selloGeneracion;
    private String idDepartamentoEmisor;
    private float impuesto;
    private float montGDescVentExentas;
    ArrayList< Contactos> contactos = new ArrayList<>();
    private String nrcCliente;
    private String otrosDocDescri;
    private String cajaSuc;
    private String observacionesDte;
    private String numIdTransp;
    private String idMunicipioReceptor;
    private String plazo;
    private float ventasGravadas0;
    private String formatodocumento;
    private String nitCliente;
    private String docRelNum;
    private float subTotalVentasNoSujetas;
    private String campo1;
    private String campo3;
    private String campo2;
    private String campo4;
    private String tipoCatContribuyente;
    private String codContingencia;
    private float totalAPagar;
    private String responsableEmisor;
    private float totOtroMonNoAfec;
    private String codSucE;
    private String numDocReceptor;
    private String resInicio;
    private String emailE;
    private String giro;
    ArrayList<Detalle> detalle = new ArrayList<>();

    public Json() {
        this.direccionEmisor = "";
        this.responsableReceptor = "";
        this.seguro = 0;
        this.cantidadTotal = 0;
        this.pdv = "";
        this.resFin = "";
        this.fovial = 0;
        this.totalExportaciones = 0;
        this.direccionCliente = "";
        this.otrosDocIdent = "";
        this.correlativoInterno = "";
        this.subTotalVentasGravadas = 0;
        this.ventaTotal = 0;
        this.contribucionSeguridad = 0;
        this.tipoItemExpor = "";
        this.nit = "";
        this.ventasGravadas = 0;
        this.cesc = 0;
        this.nrc = "";
        this.CCFAnterior = "";
        this.numeroTransaccion = "";
        this.municipio = "";
        this.montGDescVentGrav = 0;
        this.nombreComercialCl = "";
        this.fechaEmision = "";
        this.ventCterNit = "";
        this.idMunicipioEmisor = "";
        this.idDepartamentoReceptor = "";
        this.codigoActividadEconomica = "";
        this.tipoDocumento = "";
        this.fechaEnvio = "";
        this.duiCliente = "";
        this.sumas = 0;
        this.numIdenConductor = "";
        this.vtaACuentaDe = "";
        this.nomConductor = "";
        this.renta = 0;
        this.nombrePais = "";
        this.ventCterNombre = "";
        this.bienTitulo = "";
        this.numeroControl = "";
        this.nombreUsuario = "";
        this.numDocEmisor = "";
        this.ventasExentas = 0;
        this.codicionPago = "";
        this.numControl = "";
        this.docRelFecha = "";
        this.nombreCliente = "";
        this.ivaRetenido13 = 0;
        this.ivaRetenido1 = 0;
        this.noFecha = "";
        this.ventasNoGravadas = 0;
        this.montoLetras = "";
        this.ventasNoSujetas = 0;
        this.codPais = "";
        this.codGeneracion = "";
        this.telsucE = "";
        this.campoExtFE = "";
        this.montGDescVentNoSujetas = 0;
        this.selloGeneracion = "";
        this.idDepartamentoEmisor = "";
        this.impuesto = 0;
        this.montGDescVentExentas = 0;
        this.nrcCliente = "";
        this.otrosDocDescri = "";
        this.cajaSuc = "";
        this.observacionesDte = "";
        this.numIdTransp = "";
        this.idMunicipioReceptor = "";
        this.plazo = "";
        this.ventasGravadas0 = 0;
        this.formatodocumento = "";
        this.nitCliente = "";
        this.docRelNum = "";
        this.subTotalVentasNoSujetas = 0;
        this.campo1 = "";
        this.campo3 = "";
        this.campo2 = "";
        this.campo4 = "";
        this.tipoCatContribuyente = "";
        this.codContingencia = "";
        this.totalAPagar = 0;
        this.responsableEmisor = "";
        this.totOtroMonNoAfec = 0;
        this.codSucE = "";
        this.numDocReceptor = "";
        this.resInicio = "";
        this.emailE = "";
        this.giro = "";
        this.tipoTransmision = "";
        this.cotrans = 0;
        this.docRelTipo = "";
        this.impuestoEspecifico = 0;
        this.abonos = 0;
        this.ivaPercibido1 = 0;
        this.formaPago = "";
        this.ivaPercibido2 = 0;
        this.descuentos = 0;
        this.fechaHoraGeneracion = "";
        this.tipoPersona = "";
        this.resolucion = "";
        this.mostrarTributo = true;
        this.numFactura = "";
        this.saldoCapital = 0;
        this.modeloFacturacion = "";
        this.codigoGeneracion = "";
        this.subTotalVentasExentas = 0;
        this.correoUsuario = "";
        this.iva = 0;
        this.ventasGravadas13 = 0;
        this.resFecha = "";
        this.flete = 0;
        this.contribucionTurismo5 = 0;
        this.contribucionTurismo7 = 0;
        this.modTransp = "";
        this.terminal = "";
        this.motivoContin = "";
        this.tipoDocumentoReceptor = " ";
        this.codigoUsuario = "";
        this.notaRemision = "";
        this.codigoCliente = "";
        this.serie = "";
        this.departamento = "";
    }

    public String getDireccionEmisor() {
        return direccionEmisor;
    }

    public void setDireccionEmisor(String direccionEmisor) {
        this.direccionEmisor = direccionEmisor;
    }

    public String getResponsableReceptor() {
        return responsableReceptor;
    }

    public void setResponsableReceptor(String responsableReceptor) {
        this.responsableReceptor = responsableReceptor;
    }

    public float getSeguro() {
        return seguro;
    }

    public void setSeguro(float seguro) {
        this.seguro = seguro;
    }

    public float getCantidadTotal() {
        return cantidadTotal;
    }

    public void setCantidadTotal(float cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }

    public String getPdv() {
        return pdv;
    }

    public void setPdv(String pdv) {
        this.pdv = pdv;
    }

    public String getResFin() {
        return resFin;
    }

    public void setResFin(String resFin) {
        this.resFin = resFin;
    }

    public float getFovial() {
        return fovial;
    }

    public void setFovial(float fovial) {
        this.fovial = fovial;
    }

    public float getTotalExportaciones() {
        return totalExportaciones;
    }

    public void setTotalExportaciones(float totalExportaciones) {
        this.totalExportaciones = totalExportaciones;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public String getOtrosDocIdent() {
        return otrosDocIdent;
    }

    public void setOtrosDocIdent(String otrosDocIdent) {
        this.otrosDocIdent = otrosDocIdent;
    }

    public String getCorrelativoInterno() {
        return correlativoInterno;
    }

    public void setCorrelativoInterno(String correlativoInterno) {
        this.correlativoInterno = correlativoInterno;
    }

    public float getSubTotalVentasGravadas() {
        return subTotalVentasGravadas;
    }

    public void setSubTotalVentasGravadas(float subTotalVentasGravadas) {
        this.subTotalVentasGravadas = subTotalVentasGravadas;
    }

    public float getVentaTotal() {
        return ventaTotal;
    }

    public void setVentaTotal(float ventaTotal) {
        this.ventaTotal = ventaTotal;
    }

    public float getContribucionSeguridad() {
        return contribucionSeguridad;
    }

    public void setContribucionSeguridad(float contribucionSeguridad) {
        this.contribucionSeguridad = contribucionSeguridad;
    }

    public String getTipoItemExpor() {
        return tipoItemExpor;
    }

    public void setTipoItemExpor(String tipoItemExpor) {
        this.tipoItemExpor = tipoItemExpor;
    }

    public String getNit() {
        return this.nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public float getVentasGravadas() {
        return ventasGravadas;
    }

    public void setVentasGravadas(float ventasGravadas) {
        this.ventasGravadas = ventasGravadas;
    }

    public float getCesc() {
        return cesc;
    }

    public void setCesc(float cesc) {
        this.cesc = cesc;
    }

    public String getNrc() {
        return nrc;
    }

    public void setNrc(String nrc) {
        this.nrc = nrc;
    }

    public String getCCFAnterior() {
        return CCFAnterior;
    }

    public void setCCFAnterior(String CCFAnterior) {
        this.CCFAnterior = CCFAnterior;
    }

    public String getNumeroTransaccion() {
        return numeroTransaccion;
    }

    public void setNumeroTransaccion(String numeroTransaccion) {
        this.numeroTransaccion = numeroTransaccion;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public float getMontGDescVentGrav() {
        return montGDescVentGrav;
    }

    public void setMontGDescVentGrav(float montGDescVentGrav) {
        this.montGDescVentGrav = montGDescVentGrav;
    }

    public String getNombreComercialCl() {
        return nombreComercialCl;
    }

    public void setNombreComercialCl(String nombreComercialCl) {
        this.nombreComercialCl = nombreComercialCl;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getVentCterNit() {
        return ventCterNit;
    }

    public void setVentCterNit(String ventCterNit) {
        this.ventCterNit = ventCterNit;
    }

    public String getIdMunicipioEmisor() {
        return idMunicipioEmisor;
    }

    public void setIdMunicipioEmisor(String idMunicipioEmisor) {
        this.idMunicipioEmisor = idMunicipioEmisor;
    }

    public String getIdDepartamentoReceptor() {
        return idDepartamentoReceptor;
    }

    public void setIdDepartamentoReceptor(String idDepartamentoReceptor) {
        this.idDepartamentoReceptor = idDepartamentoReceptor;
    }

    public String getCodigoActividadEconomica() {
        return codigoActividadEconomica;
    }

    public void setCodigoActividadEconomica(String codigoActividadEconomica) {
        this.codigoActividadEconomica = codigoActividadEconomica;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(String fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getDuiCliente() {
        return duiCliente;
    }

    public void setDuiCliente(String duiCliente) {
        this.duiCliente = duiCliente;
    }

    public float getSumas() {
        return sumas;
    }

    public void setSumas(float sumas) {
        this.sumas = sumas;
    }

    public String getNumIdenConductor() {
        return numIdenConductor;
    }

    public void setNumIdenConductor(String numIdenConductor) {
        this.numIdenConductor = numIdenConductor;
    }

    public String getVtaACuentaDe() {
        return vtaACuentaDe;
    }

    public void setVtaACuentaDe(String vtaACuentaDe) {
        this.vtaACuentaDe = vtaACuentaDe;
    }

    public String getNomConductor() {
        return nomConductor;
    }

    public void setNomConductor(String nomConductor) {
        this.nomConductor = nomConductor;
    }

    public float getRenta() {
        return renta;
    }

    public void setRenta(float renta) {
        this.renta = renta;
    }

    public String getNombrePais() {
        return nombrePais;
    }

    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }

    public String getVentCterNombre() {
        return ventCterNombre;
    }

    public void setVentCterNombre(String ventCterNombre) {
        this.ventCterNombre = ventCterNombre;
    }

    public String getBienTitulo() {
        return bienTitulo;
    }

    public void setBienTitulo(String bienTitulo) {
        this.bienTitulo = bienTitulo;
    }

    public String getNumeroControl() {
        return numeroControl;
    }

    public void setNumeroControl(String numeroControl) {
        this.numeroControl = numeroControl;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNumDocEmisor() {
        return numDocEmisor;
    }

    public void setNumDocEmisor(String numDocEmisor) {
        this.numDocEmisor = numDocEmisor;
    }

    public float getVentasExentas() {
        return ventasExentas;
    }

    public void setVentasExentas(float ventasExentas) {
        this.ventasExentas = ventasExentas;
    }

    public String getCodicionPago() {
        return codicionPago;
    }

    public void setCodicionPago(String codicionPago) {
        this.codicionPago = codicionPago;
    }

    public String getNumControl() {
        return numControl;
    }

    public void setNumControl(String numControl) {
        this.numControl = numControl;
    }

    public String getDocRelFecha() {
        return docRelFecha;
    }

    public void setDocRelFecha(String docRelFecha) {
        this.docRelFecha = docRelFecha;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public float getIvaRetenido13() {
        return ivaRetenido13;
    }

    public void setIvaRetenido13(float ivaRetenido13) {
        this.ivaRetenido13 = ivaRetenido13;
    }

    public float getIvaRetenido1() {
        return ivaRetenido1;
    }

    public void setIvaRetenido1(float ivaRetenido1) {
        this.ivaRetenido1 = ivaRetenido1;
    }

    public String getNoFecha() {
        return noFecha;
    }

    public void setNoFecha(String noFecha) {
        this.noFecha = noFecha;
    }

    public float getVentasNoGravadas() {
        return ventasNoGravadas;
    }

    public void setVentasNoGravadas(float ventasNoGravadas) {
        this.ventasNoGravadas = ventasNoGravadas;
    }

    public String getMontoLetras() {
        return montoLetras;
    }

    public void setMontoLetras(String montoLetras) {
        this.montoLetras = montoLetras;
    }

    public float getVentasNoSujetas() {
        return ventasNoSujetas;
    }

    public void setVentasNoSujetas(float ventasNoSujetas) {
        this.ventasNoSujetas = ventasNoSujetas;
    }

    public String getCodPais() {
        return codPais;
    }

    public void setCodPais(String codPais) {
        this.codPais = codPais;
    }

    public String getCodGeneracion() {
        return codGeneracion;
    }

    public void setCodGeneracion(String codGeneracion) {
        this.codGeneracion = codGeneracion;
    }

    public String getTelsucE() {
        return telsucE;
    }

    public void setTelsucE(String telsucE) {
        this.telsucE = telsucE;
    }

    public String getCampoExtFE() {
        return campoExtFE;
    }

    public void setCampoExtFE(String campoExtFE) {
        this.campoExtFE = campoExtFE;
    }

    public float getMontGDescVentNoSujetas() {
        return montGDescVentNoSujetas;
    }

    public void setMontGDescVentNoSujetas(float montGDescVentNoSujetas) {
        this.montGDescVentNoSujetas = montGDescVentNoSujetas;
    }

    public String getSelloGeneracion() {
        return selloGeneracion;
    }

    public void setSelloGeneracion(String selloGeneracion) {
        this.selloGeneracion = selloGeneracion;
    }

    public String getIdDepartamentoEmisor() {
        return idDepartamentoEmisor;
    }

    public void setIdDepartamentoEmisor(String idDepartamentoEmisor) {
        this.idDepartamentoEmisor = idDepartamentoEmisor;
    }

    public float getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(float impuesto) {
        this.impuesto = impuesto;
    }

    public float getMontGDescVentExentas() {
        return montGDescVentExentas;
    }

    public void setMontGDescVentExentas(float montGDescVentExentas) {
        this.montGDescVentExentas = montGDescVentExentas;
    }

    public ArrayList<Contactos> getContactos() {
        return contactos;
    }

    public void setContactos(ArrayList<Contactos> contactos) {
        this.contactos = contactos;
    }

    public String getNrcCliente() {
        return nrcCliente;
    }

    public void setNrcCliente(String nrcCliente) {
        this.nrcCliente = nrcCliente;
    }

    public String getOtrosDocDescri() {
        return otrosDocDescri;
    }

    public void setOtrosDocDescri(String otrosDocDescri) {
        this.otrosDocDescri = otrosDocDescri;
    }

    public String getCajaSuc() {
        return cajaSuc;
    }

    public void setCajaSuc(String cajaSuc) {
        this.cajaSuc = cajaSuc;
    }

    public String getObservacionesDte() {
        return observacionesDte;
    }

    public void setObservacionesDte(String observacionesDte) {
        this.observacionesDte = observacionesDte;
    }

    public String getNumIdTransp() {
        return numIdTransp;
    }

    public void setNumIdTransp(String numIdTransp) {
        this.numIdTransp = numIdTransp;
    }

    public String getIdMunicipioReceptor() {
        return idMunicipioReceptor;
    }

    public void setIdMunicipioReceptor(String idMunicipioReceptor) {
        this.idMunicipioReceptor = idMunicipioReceptor;
    }

    public String getPlazo() {
        return plazo;
    }

    public void setPlazo(String plazo) {
        this.plazo = plazo;
    }

    public float getVentasGravadas0() {
        return ventasGravadas0;
    }

    public void setVentasGravadas0(float ventasGravadas0) {
        this.ventasGravadas0 = ventasGravadas0;
    }

    public String getFormatodocumento() {
        return formatodocumento;
    }

    public void setFormatodocumento(String formatodocumento) {
        this.formatodocumento = formatodocumento;
    }

    public String getNitCliente() {
        return nitCliente;
    }

    public void setNitCliente(String nitCliente) {
        this.nitCliente = nitCliente;
    }

    public String getDocRelNum() {
        return docRelNum;
    }

    public void setDocRelNum(String docRelNum) {
        this.docRelNum = docRelNum;
    }

    public float getSubTotalVentasNoSujetas() {
        return subTotalVentasNoSujetas;
    }

    public void setSubTotalVentasNoSujetas(float subTotalVentasNoSujetas) {
        this.subTotalVentasNoSujetas = subTotalVentasNoSujetas;
    }

    public String getCampo1() {
        return campo1;
    }

    public void setCampo1(String campo1) {
        this.campo1 = campo1;
    }

    public String getCampo3() {
        return campo3;
    }

    public void setCampo3(String campo3) {
        this.campo3 = campo3;
    }

    public String getCampo2() {
        return campo2;
    }

    public void setCampo2(String campo2) {
        this.campo2 = campo2;
    }

    public String getCampo4() {
        return campo4;
    }

    public void setCampo4(String campo4) {
        this.campo4 = campo4;
    }

    public String getTipoCatContribuyente() {
        return tipoCatContribuyente;
    }

    public void setTipoCatContribuyente(String tipoCatContribuyente) {
        this.tipoCatContribuyente = tipoCatContribuyente;
    }

    public String getCodContingencia() {
        return codContingencia;
    }

    public void setCodContingencia(String codContingencia) {
        this.codContingencia = codContingencia;
    }

    public float getTotalAPagar() {
        return totalAPagar;
    }

    public void setTotalAPagar(float totalAPagar) {
        this.totalAPagar = totalAPagar;
    }

    public String getResponsableEmisor() {
        return responsableEmisor;
    }

    public void setResponsableEmisor(String responsableEmisor) {
        this.responsableEmisor = responsableEmisor;
    }

    public float getTotOtroMonNoAfec() {
        return totOtroMonNoAfec;
    }

    public void setTotOtroMonNoAfec(float totOtroMonNoAfec) {
        this.totOtroMonNoAfec = totOtroMonNoAfec;
    }

    public String getCodSucE() {
        return codSucE;
    }

    public void setCodSucE(String codSucE) {
        this.codSucE = codSucE;
    }

    public String getNumDocReceptor() {
        return numDocReceptor;
    }

    public void setNumDocReceptor(String numDocReceptor) {
        this.numDocReceptor = numDocReceptor;
    }

    public String getResInicio() {
        return resInicio;
    }

    public void setResInicio(String resInicio) {
        this.resInicio = resInicio;
    }

    public String getEmailE() {
        return emailE;
    }

    public void setEmailE(String emailE) {
        this.emailE = emailE;
    }

    public String getGiro() {
        return giro;
    }

    public void setGiro(String giro) {
        this.giro = giro;
    }

    public ArrayList<Detalle> getDetalle() {
        return detalle;
    }

    public void setDetalle(ArrayList<Detalle> detalle) {
        this.detalle = detalle;
    }

    public String getTipoTransmision() {
        return tipoTransmision;
    }

    public void setTipoTransmision(String tipoTransmision) {
        this.tipoTransmision = tipoTransmision;
    }

    public float getCotrans() {
        return cotrans;
    }

    public void setCotrans(float cotrans) {
        this.cotrans = cotrans;
    }

    public String getDocRelTipo() {
        return docRelTipo;
    }

    public void setDocRelTipo(String docRelTipo) {
        this.docRelTipo = docRelTipo;
    }

    public float getImpuestoEspecifico() {
        return impuestoEspecifico;
    }

    public void setImpuestoEspecifico(float impuestoEspecifico) {
        this.impuestoEspecifico = impuestoEspecifico;
    }

    public float getAbonos() {
        return abonos;
    }

    public void setAbonos(float abonos) {
        this.abonos = abonos;
    }

    public float getIvaPercibido1() {
        return ivaPercibido1;
    }

    public void setIvaPercibido1(float ivaPercibido1) {
        this.ivaPercibido1 = ivaPercibido1;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public float getIvaPercibido2() {
        return ivaPercibido2;
    }

    public void setIvaPercibido2(float ivaPercibido2) {
        this.ivaPercibido2 = ivaPercibido2;
    }

    public float getDescuentos() {
        return descuentos;
    }

    public void setDescuentos(float descuentos) {
        this.descuentos = descuentos;
    }

    public String getFechaHoraGeneracion() {
        return fechaHoraGeneracion;
    }

    public void setFechaHoraGeneracion(String fechaHoraGeneracion) {
        this.fechaHoraGeneracion = fechaHoraGeneracion;
    }

    public String getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(String tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public String getResolucion() {
        return resolucion;
    }

    public void setResolucion(String resolucion) {
        this.resolucion = resolucion;
    }

    public boolean isMostrarTributo() {
        return mostrarTributo;
    }

    public void setMostrarTributo(boolean mostrarTributo) {
        this.mostrarTributo = mostrarTributo;
    }

    public String getNumFactura() {
        return numFactura;
    }

    public void setNumFactura(String numFactura) {
        this.numFactura = numFactura;
    }

    public float getSaldoCapital() {
        return saldoCapital;
    }

    public void setSaldoCapital(float saldoCapital) {
        this.saldoCapital = saldoCapital;
    }

    public String getModeloFacturacion() {
        return modeloFacturacion;
    }

    public void setModeloFacturacion(String modeloFacturacion) {
        this.modeloFacturacion = modeloFacturacion;
    }

    public String getCodigoGeneracion() {
        return codigoGeneracion;
    }

    public void setCodigoGeneracion(String codigoGeneracion) {
        this.codigoGeneracion = codigoGeneracion;
    }

    public float getSubTotalVentasExentas() {
        return subTotalVentasExentas;
    }

    public void setSubTotalVentasExentas(float subTotalVentasExentas) {
        this.subTotalVentasExentas = subTotalVentasExentas;
    }

    public ArrayList<Object> getArTributos() {
        return arTributos;
    }

    public void setArTributos(ArrayList<Object> arTributos) {
        this.arTributos = arTributos;
    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public void setCorreoUsuario(String correoUsuario) {
        this.correoUsuario = correoUsuario;
    }

    public float getIva() {
        return iva;
    }

    public void setIva(float iva) {
        this.iva = iva;
    }

    public float getVentasGravadas13() {
        return ventasGravadas13;
    }

    public void setVentasGravadas13(float ventasGravadas13) {
        this.ventasGravadas13 = ventasGravadas13;
    }

    public String getResFecha() {
        return resFecha;
    }

    public void setResFecha(String resFecha) {
        this.resFecha = resFecha;
    }

    public float getFlete() {
        return flete;
    }

    public void setFlete(float flete) {
        this.flete = flete;
    }

    public float getContribucionTurismo5() {
        return contribucionTurismo5;
    }

    public void setContribucionTurismo5(float contribucionTurismo5) {
        this.contribucionTurismo5 = contribucionTurismo5;
    }

    public float getContribucionTurismo7() {
        return contribucionTurismo7;
    }

    public void setContribucionTurismo7(float contribucionTurismo7) {
        this.contribucionTurismo7 = contribucionTurismo7;
    }

    public String getModTransp() {
        return modTransp;
    }

    public void setModTransp(String modTransp) {
        this.modTransp = modTransp;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getMotivoContin() {
        return motivoContin;
    }

    public void setMotivoContin(String motivoContin) {
        this.motivoContin = motivoContin;
    }

    public String getTipoDocumentoReceptor() {
        return tipoDocumentoReceptor;
    }

    public void setTipoDocumentoReceptor(String tipoDocumentoReceptor) {
        this.tipoDocumentoReceptor = tipoDocumentoReceptor;
    }

    public String getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(String codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public String getNotaRemision() {
        return notaRemision;
    }

    public void setNotaRemision(String notaRemision) {
        this.notaRemision = notaRemision;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
    private String tipoTransmision;
    private float cotrans;
    private String docRelTipo;
    private float impuestoEspecifico;
    private float abonos;
    private float ivaPercibido1;
    private String formaPago;
    private float ivaPercibido2;
    private float descuentos;
    private String fechaHoraGeneracion;
    private String tipoPersona;
    private String resolucion;
    private boolean mostrarTributo;
    private String numFactura;
    private float saldoCapital;
    private String modeloFacturacion;
    private String codigoGeneracion;
    private float subTotalVentasExentas;
    ArrayList< Object> arTributos = new ArrayList< Object>();
    private String correoUsuario;
    private float iva;
    private float ventasGravadas13;
    private String resFecha;
    private float flete;
    private float contribucionTurismo5;
    private float contribucionTurismo7;
    private String modTransp;
    private String terminal;
    private String motivoContin;
    private String tipoDocumentoReceptor;
    private String codigoUsuario;
    private String notaRemision;
    private String codigoCliente;
    private String serie;
    private String departamento;

}
