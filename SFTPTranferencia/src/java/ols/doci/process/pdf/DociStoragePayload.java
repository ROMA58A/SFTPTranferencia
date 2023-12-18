/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ols.doci.process.pdf;

/**
 *
 * @author ols.kevin.osorio
 */
public class DociStoragePayload
{
    public DociStoragePayload()
    {
    }

    public DociStoragePayload(String shorttoken, String tableName, String companyid, String ambiente)
    {
        this.shorttoken = shorttoken;
        this.tableName = tableName;
        this.companyid = companyid;
        this.ambiente = ambiente;
    }

    public String shorttoken;
    public String tableName;
    public String companyid;
    public String ambiente;

    public String getShorttoken()
    {
        return shorttoken;
    }

    public void setShorttoken(String shorttoken)
    {
        this.shorttoken = shorttoken;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public String getCompanyid()
    {
        return companyid;
    }

    public void setCompanyid(String companyid)
    {
        this.companyid = companyid;
    }

    public String getAmbiente()
    {
        return ambiente;
    }

    public void setAmbiente(String ambiente)
    {
        this.ambiente = ambiente;
    }

}
