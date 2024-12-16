package es.uva.sockets;

public class Coordenadas {
    private final int x;
    private final int y;

    public Coordenadas(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Coordenadas mover(Direccion dir){
        //TODO: Devolver unas coordenadas movidas según direccion
        switch (dir) {
            case UP:
                return new Coordenadas(x, y-1);
            case DOWN:
                return new Coordenadas(x, y+1);
            case LEFT:
                return new Coordenadas(x - 1, y);
            case RIGHT:
                return new Coordenadas(x + 1, y);
        }
        return this;
    }

    public boolean equals(Coordenadas otras) {
        return (this.x == otras.x) && (this.y == otras.y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
