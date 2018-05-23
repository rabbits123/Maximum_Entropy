/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.sentiment.maxent;

/**
 *
 * @author Phu
 */
public class Feedback {
    private String content;
    private String khoa;
    private String TenGV;
    private String MaMH;
    private String TenMH;
    private String sentLabel;
    private String topLabel;

    public Feedback(String content, String khoa, String TenGV, String MaMH, String TenMH, String sentLabel, String topLabel) {
        this.content = content;
        this.khoa = khoa;
        this.TenGV = TenGV;
        this.MaMH = MaMH;
        this.TenMH = TenMH;
        this.sentLabel = sentLabel;
        this.topLabel = topLabel;
    }

    public void setSentLabel(String sentLabel) {
        this.sentLabel = sentLabel;
    }

    public void setTopLabel(String topLabel) {
        this.topLabel = topLabel;
    }


    public String getSentLabel() {
        return sentLabel;
    }

    public String getTopLabel() {
        return topLabel;
    }
    
    public void setContent(String content) {
        this.content = content;
    }

    public void setKhoa(String khoa) {
        this.khoa = khoa;
    }

    public void setTenGV(String TenGV) {
        this.TenGV = TenGV;
    }

    public void setMaMH(String MaMH) {
        this.MaMH = MaMH;
    }

    public void setTenMH(String TenMH) {
        this.TenMH = TenMH;
    }

    
    public String getContent() {
        return content;
    }

    public String getKhoa() {
        return khoa;
    }

    public String getTenGV() {
        return TenGV;
    }

    public String getMaMH() {
        return MaMH;
    }

    public String getTenMH() {
        return TenMH;
    }
    
    
    
}
