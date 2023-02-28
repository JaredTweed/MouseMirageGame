public class CrackedWall extends Wall {

    /**
     * Constructor for CrackedWall
     * sets collision is false to allow movement through and sets the image path to be loaded
     */
    public CrackedWall() {
        super();
        collision = false;
        isCrackedWall = true;
        isOpaque = true;
        imagePath = "crackedwall.png";
        getImage();
    }

    public void setImage(String imageFileName) {
//        System.out.println("trying to set cracked wall image to " + imageFileName);
        if(!imageFileName.equals("")) {
            imagePath = "/crackedwall/" + imageFileName + ".png";
        }
        getImage();
    }
}
