public class Wall extends Cell {

    /**
     * Constructor for Wall
     * sets collision to true and sets the image path to be loaded
     */
    public Wall() {
        super();
        isOpaque = true;
        collision = true;
        imagePath = "wall.png";
    }

    public void setImage(String imageFileName) {
        if(!imageFileName.equals("")) {
            imagePath = "/wall/" + imageFileName + ".png";
        }
        getImage();
    }
}