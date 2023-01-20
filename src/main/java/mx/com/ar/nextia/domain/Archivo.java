package mx.com.ar.nextia.domain;

public class Archivo {

    private String nombre;
    private String url;

    public Archivo() {
    }

    public Archivo(String nombre, String url) {
        this.nombre = nombre;
        this.url = url;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
