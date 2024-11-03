import javax.swing.*;




public class App {
    public static void main(String[] args) throws Exception {
        int boardwidth=360;
        int boardheight=640;
        JFrame frame =new JFrame("Flappy Bird");
       
       
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(boardwidth,boardheight);
        
        flappybird flappybird =new flappybird();
        frame.add(flappybird);
        frame.pack();
        frame.setLocationRelativeTo(null);
        flappybird.requestFocus();
        frame.setVisible(true);

    }
}
