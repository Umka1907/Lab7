package citis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.NONE)
public class Coordinates implements Serializable {
    @XmlElement
    private Float x; //Поле не может быть null
    @XmlElement
    private Long y; //Поле не может быть null

    public void setX(Float x) {
        this.x = x;
    }

    public void setY(Long y) {
        this.y = y;
    }

    public Float getX() {
        return x;
    }

    public Long getY() {
        return y;
    }

    @Override
    public String toString() {
        return "(" + "x= " + x + "; y= " + y + ')';
    }
}