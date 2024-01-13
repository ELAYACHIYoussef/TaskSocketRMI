import java.io.Serializable;

public class Data implements Serializable {

    byte [] f;
    int id ;
    int hi;
    int wi;
    float[] Kirnel;

    public byte[] getF() {
        return f;
    }

    public void setF(byte[] f) {
        this.f = f;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHi() {
        return hi;
    }

    public void setHi(int hi) {
        this.hi = hi;
    }

    public int getWi() {
        return wi;
    }

    public void setWi(int wi) {
        this.wi = wi;
    }

    public float[] getKirnel() {
        return Kirnel;
    }

    public void setKirnel(float[] kirnel) {
        Kirnel = kirnel;
    }
}
