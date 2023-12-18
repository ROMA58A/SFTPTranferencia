package ols.doci.process.pdf;

/**
 * @author ols.kevin.osorio
 * @author Luis Brayan
 */
public class DociPdfPayload
{
    public DociPdfPayload(DociStorageResponse source, String fullFilePath)
    {
        this.shorttoken = source.getShorttoken();
        this.companyid = source.getCompanyid();

        this.nit = source.getNit();
        this.sello = source.getSello();
        this.numerocontrol = source.getNumerocontrol();
        this.codigogeneracion = source.getCodigogeneracion();
        this.fechahoraemision = source.getFechaemision();

        this.revnum = String.valueOf(source.getRevnum());
        this.itemnum = String.valueOf(source.getItemnum());
        this.itemtypenum = String.valueOf(source.getItemtypenum());
        this.diskgroup = source.getDiskgroup();

        this.filefullpath = fullFilePath;
        this.endpath = fullFilePath;

        this.templatepath = source.getTemplatepath();
        this.outputfilename = "";//.replaceAll(".pdf", ""));
        this.s3 = source.getFilefullpath().replaceAll(".json", ".zip");
        this.geturl="2";
    }

    // properties
    private final String shorttoken;
    private final String companyid;
    private final String nit;
    private final String itemnum;
    private final String revnum;
    private final String diskgroup;
    private final String itemtypenum;
    private final String filefullpath;
    private final String s3;
    private final String templatepath;
    private final String endpath;
    private final String outputfilename;
    private final String codigogeneracion;
    private final String sello;
    private final String fechahoraemision;
    private final String numerocontrol;
    private final String geturl;
    // getters
    public String getShorttoken()
    {
        return shorttoken;
    }

    public String getCompanyid()
    {
        return companyid;
    }

    public String getNit()
    {
        return nit;
    }

    public String getItemnum()
    {
        return itemnum;
    }

    public String getRevnum()
    {
        return revnum;
    }

    public String getDiskgroup()
    {
        return diskgroup;
    }

    public String getItemtypenum()
    {
        return itemtypenum;
    }

    public String getFilefullpath()
    {
        return filefullpath;
    }

    public String getS3()
    {
        return s3;
    }

    public String getTemplatepath()
    {
        return templatepath;
    }

    public String getEndpath()
    {
        return endpath;
    }

    public String getOutputfilename()
    {
        return outputfilename;
    }

    public String getCodigogeneracion()
    {
        return codigogeneracion;
    }

    public String getSello()
    {
        return sello;
    }

    public String getFechahoraemision()
    {
        return fechahoraemision;
    }

    public String getNumerocontrol()
    {
        return numerocontrol;
    }
    
    public String getGeturl ()
            
    {
     return geturl;
    }

}
