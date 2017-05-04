
package game;

import java.awt.Image;

public class Piece {
    private PieceType type;
    private Image image;
    private Owner owner;
    private int rank;

    public enum Owner {
        Player1, Player2
    }

    public enum PieceType {
        Elephant, Camel, Horse, Dog, Cat, Rabbit
    }

    public Piece(char c) {
        if (Character.isUpperCase(c))
            createP1Piece(c);
        else
            createP2Piece(c);
    }

    private void createP2Piece(char c) {
        switch (c) {
            case 'e':
                this.type = PieceType.Elephant;
                this.owner = Owner.Player2;
                this.rank = 5;
                break;
            case 'c':
                this.type = PieceType.Camel;
                this.owner = Owner.Player2;
                this.rank = 4;
                break;
            case 'h':
                this.type = PieceType.Horse;
                this.owner = Owner.Player2;
                this.rank = 3;
                break;
            case 'd':
                this.type = PieceType.Dog;
                this.owner = Owner.Player2;
                this.rank = 2;
                break;
            case 'k':
                this.type = PieceType.Cat;
                this.owner = Owner.Player2;
                this.rank = 1;
                break;
            case 'r':
                this.type = PieceType.Rabbit;
                this.owner = Owner.Player2;
                this.rank = 0;
                break;
            default:
                System.err.println("Invalid char supplied");
        }
    }

    private void createP1Piece(char c) {
        switch (c) {
            case 'E':
                this.type = PieceType.Elephant;
                this.owner = Owner.Player1;
                this.rank = 5;
                break;
            case 'C':
                this.type = PieceType.Camel;
                this.owner = Owner.Player1;
                this.rank = 4;
                break;
            case 'H':
                this.type = PieceType.Horse;
                this.owner = Owner.Player1;
                this.rank = 3;
                break;
            case 'D':
                this.type = PieceType.Dog;
                this.owner = Owner.Player1;
                this.rank = 2;
                break;
            case 'K':
                this.type = PieceType.Cat;
                this.owner = Owner.Player1;
                this.rank = 1;
                break;
            case 'R':
                this.type = PieceType.Rabbit;
                this.owner = Owner.Player1;
                this.rank = 0;
                break;

            default:
                System.err.println("Invalid char supplied");
        }
    }

    public Piece(PieceType t, Image i, Owner o) {
        this.type = t;
        this.image = i;
        this.owner = o;
    }

    public PieceType getType() {
        return this.type;
    }

    public void setType(PieceType type) {
        this.type = type;
    }

    public Image getImage() {
        return this.image;
    }

    public void setImg(Image img) {
        this.image = img;
    }

    public Owner getOwner() {
        return this.owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public int getRank() {
        return this.rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public boolean equals(Object p2) {
        if ((p2 instanceof Piece)) {
            if (((Piece) p2).getType() == this.getType()
                    && (((Piece) p2).getOwner() == this.getOwner())) {
                return true;
            }
        }
        return false;

    }

    public boolean isStrongerThan(Piece p2) {
        return (this.getRank() > p2.getRank());
    }
}
