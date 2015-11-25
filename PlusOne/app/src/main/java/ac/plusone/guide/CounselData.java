package ac.plusone.guide;

/**
 * Created by jang on 2015-11-20.
 */
public class CounselData {

    public String CTGRY_NM = null;
    public String LOCAL = null;
    public String GOV_NM = null;
    public String DIV = null;
    public String ADDR = null;
    public String CALL_NUL = null;

    public CounselData() {
    }

    public String getCTGRY_NM() {
        return CTGRY_NM;
    }

    public void setCTGRY_NM(String CTGRY_NM) {
        this.CTGRY_NM = CTGRY_NM;
    }

    public String getLOCAL() {
        return LOCAL;
    }

    public void setLOCAL(String LOCAL) {
        this.LOCAL = LOCAL;
    }

    public String getGOV_NM() {
        return GOV_NM;
    }

    public void setGOV_NM(String GOV_NM) {
        this.GOV_NM = GOV_NM;
    }

    public String getDIV() {
        return DIV;
    }

    public void setDIV(String DIV) {
        this.DIV = DIV;
    }

    public String getADDR() {
        return ADDR;
    }

    public void setADDR(String ADDR) {
        this.ADDR = ADDR;
    }

    public String getCALL_NUL() {
        return CALL_NUL;
    }

    public void setCALL_NUL(String CALL_NUL) {
        this.CALL_NUL = CALL_NUL;
    }

    @Override
    public String toString() {
        return "CounselData{" +
                "CTGRY_NM='" + CTGRY_NM + '\'' +
                ", LOCAL='" + LOCAL + '\'' +
                ", GOV_NM='" + GOV_NM + '\'' +
                ", DIV='" + DIV + '\'' +
                ", ADDR='" + ADDR + '\'' +
                ", CALL_NUL='" + CALL_NUL + '\'' +
                '}';
    }
}
