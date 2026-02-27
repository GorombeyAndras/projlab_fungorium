import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/*
* Az MVC Model View része. A megjelenítésért felelős és
* a felhasználóval történő interakcióért.
 */
public class View {

    private static Controller controller = new Controller();
    private static List<TectonView> tectonViewList;
    private static List<SporeView> sporeViewList;
    private static List<InsectView> insectViewList;
    private static List<InterconnectingHyphaView> interViewList;
    private static List<IngrownHyphaView> ingViewList;
    private static List<MushroomBodyView> bodyViewList;

    private static GameCanvas canvas;
    private static JButton executeButton;
    private static List<JButton> menuButtonList;
    private static List<JButton> buggerButtonList;
    private static List<JButton> mushroomButtonList;
    private static List<JLabel> labelList;
    private static JComboBox fromComboBox;
    private static JComboBox toComboBox;
    private static JComboBox thirdoptionComboBox;
    private static JComboBox insectComboBox;
    private static JPanel menuPanel;
    private static JPanel sidePanel;
    private static JPanel bottomRightPanel;
    private static JLabel playerLabel;
    private static JLabel fromLabel;
    private static JLabel toLabel;
    private static JLabel thirdoptionLabel;
    private static JLabel iLabel;
    private static JFrame frame;
    private static JLabel scoreLabel;

    // Startgame
    private static JComboBox insectPlayerComboBox;
    private static JComboBox mushroomPlayerComboBox;
    private static JLabel insectPlayerLabel;
    private static JLabel mushroomPlayerLabel;
    private static JButton startGameButton;

    private static Color emeraldColor = new Color(1,50,32);
    private static Color pyroxeneColor = new Color(255,140,0);
    private static Color anorthiteColor= new Color(139,0,0);
    private static Color tanzaniteColor= new Color(196,145,2);

    private static HashMap<Class,Color> colorMap = new HashMap<>();

    private static Color sporeColor= new Color(0,255,0);
    private static Color bodyColor= new Color(99, 151, 54);

    /*
    * A View lehetséges állapotai
     */
    enum State{
        growHypha,
        growBody,
        disperseSpore,
        eatInsect,
        move,
        cutHypha,
        eatSpore,
        statelessmushroomer,
        statelessbugger,
        startgame
    }
    static State state = State.startgame;

    /*
    * getter függvény
     */
    public static Color getSporeColor(){
        return sporeColor;
    }
    /*
     * getter függvény
     */
    public static Color getBodyColor(){
        return bodyColor;
    }
    /*
     * getter függvény
     */
    public static Color getTectonColor(Class c){
        return colorMap.get(c);
    }

    private static String currentPlayer;
    /*
    * Frissíti a jelenleg lépő játékos kilétét
     */
    public static void updatePlayer(){
        currentPlayer = Model.getCurrentPlayer();
        if(currentPlayer != null){
            playerLabel.setText("Current Player: " + currentPlayer);
            playerLabel.updateUI();
            if(currentPlayer.startsWith("G")){
                state = State.statelessmushroomer;
            }else if(currentPlayer.startsWith("B")){
                state = State.statelessbugger;
            }
            stateChanged();
        }

    }
    /*
    * A fő canvas amin az elemek találhatók
     */
    private static class GameCanvas extends Canvas {
        private boolean repainted=false;
        public boolean isRepainted(){
            return repainted;
        }
        public void setRepainted(Boolean _repainted){
            repainted=_repainted;
        }
        /*
        * konstruktor függvény
         */
        public GameCanvas() {
            setBackground(Color.BLUE); // háttérszín
            setBounds(0,0,600,400);

        }
        /*
        * A kirajzolásért felelős fv
         */
        public void paint(Graphics g) {
            if(!repainted){
                this.repaint();
                repainted=true;
            }
            for(TectonView v: tectonViewList){
                v.draw(g);
            }
            try {
                Thread.sleep(75);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            for(TectonView v: tectonViewList){
                v.drawText(g);
            }
            for(IngrownHyphaView v: ingViewList){
                v.draw(g);
            }
            for(InterconnectingHyphaView v: interViewList){
                v.draw(g);
            }

            for(MushroomBodyView v: bodyViewList){
                v.draw(g);
            }
            for(InsectView v: insectViewList){
                v.draw(g);
            }
        }
    }
    /*
     * getter függvény
     */
    public static List<SporeView> getSporeViewList() {
        return sporeViewList;
    }
    /*
    * Setupolja a pályát
     */
    public static void Setup(){
        tectonViewList = new ArrayList<>();
        sporeViewList = new ArrayList<>();
        ingViewList = new ArrayList<>();
        interViewList = new ArrayList<>();
        insectViewList = new ArrayList<>();
        bodyViewList = new ArrayList<>();

        colorMap.put(Emerald.class,emeraldColor);
        colorMap.put(Pyroxene.class,pyroxeneColor);
        colorMap.put(Tanzanite.class,tanzaniteColor);
        colorMap.put(Anorthite.class,anorthiteColor);

        frame = new JFrame("Fungorium");
        frame.setSize(800,600);
        //frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);

        menuButtonList = new ArrayList<>();
        mushroomButtonList = new ArrayList<>();
        buggerButtonList = new ArrayList<>();
        labelList= new ArrayList<>();

        // Canvas a játékelemek megjelenítésére
        canvas = new GameCanvas();

        frame.add(canvas);

        // Menüpanel gombjai
        JButton exitButton = new JButton("EXIT");
        JButton newGameButton = new JButton("START NEW GAME");
        JButton ruleButton = new JButton("RULES");

        //A gombok hozzáadása a gomblistához
        menuButtonList.add(exitButton);
        menuButtonList.add(newGameButton);
        menuButtonList.add(ruleButton);


        // Menüpanel, a lent található cyan panel
        menuPanel = new JPanel();
        menuPanel.setBackground(Color.CYAN);
        menuPanel.setLayout(null);


        scoreLabel = new JLabel("Score: "+ Model.getPlayerScore(currentPlayer));
        labelList.add(scoreLabel);
        scoreLabel.setBounds(275,25,100,20);
        menuPanel.add(scoreLabel);


        JLabel insectLabel = new JLabel("oε : Insect");
        insectLabel.setBounds(75,80,100,20);
        menuPanel.add(insectLabel);


        JLabel sporeLabel = new JLabel("● : Spore");
        sporeLabel.setBounds(160,80,100,20);
        menuPanel.add(sporeLabel);


        JLabel bodyLabel = new JLabel("■ : MushroomBody");
        bodyLabel.setBounds(250,80,125,20);
        menuPanel.add(bodyLabel);


        JLabel hyphaLabel = new JLabel("/  : Mushroom Hypha");
        hyphaLabel.setBounds(400,80,125,20);
        menuPanel.add(hyphaLabel);

        labelList.add(insectLabel);
        labelList.add(sporeLabel);
        labelList.add(bodyLabel);
        labelList.add(hyphaLabel);

        exitButton.setBounds(100,50,100,20);
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menuPanel.add(exitButton,BorderLayout.SOUTH);

        newGameButton.setBounds(225,50,150,20);
        newGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                state = State.startgame;
                stateChanged();
            }
        });
        menuPanel.add(newGameButton,BorderLayout.SOUTH);

        ruleButton.setBounds(400,50,100,20);
        ruleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame ruleWindow = new JFrame("Rules");
                JTextArea ruleTextArea = new JTextArea("RULES\n" +
                        "\n" +
                        "Tectons:\n" +
                        "- Anorthite (dark red): earthquakes, disappearing hyphas\n" +
                        "- Pyroxene (dark orange): crossing hyphas\n" +
                        "- Tanzanite (dark mustard): no mushroombody\n" +
                        "- Emerald (dark green): keeps the hyphas alive on it\n" +
                        "- All can break into two, then all the insects, spores,"+"\n"+
                        "  mushroombodies and hyphas will still exist on one of the two post-tectons.\n" +
                        "\n" +
                        "Mushrooms:\n" +
                        "- Hyphas can grow continuously from the mushroom\n" +
                        "- Spores can make them grow further\n" +
                        "- Growing bodies gives you points, but you need to consume spores on the targeted tecton to do so\n" +
                        "- Bodies can disperse spores\n" +
                        "- Hyphas can eat stunned Insects, gaining points\n" +
                        "\n" +
                        "Buggers:\n" +
                        "- You control your Insects (bugs)\n" +
                        "- Bugs can move from tecton to tecton through connecting hyphas\n" +
                        "- Bugs can eat spores gaining their \"food\" value as points, also gaining their perks\n" +
                        "- Bugs can cut hyphas\n" +
                        "\n" +
                        "Spores:\n" +
                        "You do not know what type of spore you eat\n" +
                        "Types of spores:\n" +
                        "- Vortex: makes you faster – 1p\n" +
                        "- Splitspore: duplicates the bug – 1p\n" +
                        "- Clogspore: disables cutting ability – 3p\n" +
                        "- Dropswap: stops movement – 5p\n" +
                        "- Frostdash: freezes the bug – 7p\n" +
                        "All effects (except duplication) expire after a while\n" +
                        "\n" +
                        "Winning conditions:\n" +
                        "- Mushroomers:\n" +
                        "- The mushroomer with the most points wins\n" +
                        "- Point sources: growing body or eating bug\n" +
                        "\n" +
                        "- Buggers:\n" +
                        "- The bugger with the most points wins\n" +
                        "- Point sources: eating spores\n" +
                        "\n");
                ruleTextArea.setEditable(false);
                ruleWindow.add(ruleTextArea);
                ruleTextArea.setBackground(Color.GRAY);
                ruleTextArea.setForeground(Color.BLACK);
                ruleWindow.setSize(575, 700);
                ruleWindow.setResizable(false);
                ruleWindow.setLocationRelativeTo(null); // Center on screen
                ruleWindow.setVisible(true);
            }
        });
        menuPanel.add(ruleButton,BorderLayout.SOUTH);

        menuPanel.setBounds(0,400,600,200);
        frame.add(menuPanel,BorderLayout.WEST);

        /****************  Sidepanel, a jobbra található piros panel ***************/
        sidePanel = new JPanel();
        sidePanel.setBackground(Color.RED);
        sidePanel.setBounds(600,0,200,400);

        playerLabel = new JLabel("Current Player: " + currentPlayer);
        labelList.add(playerLabel);
        playerLabel.setBounds(50,25,100,20);
        sidePanel.add(playerLabel, BorderLayout.NORTH);

        bottomRightPanel = new JPanel();
        bottomRightPanel.setBounds(600,400,200,200);
        bottomRightPanel.setBackground(Color.RED);

        //Mushroomerhez kötődő gombok
        JButton growHyphaButton = new JButton("GROW HYPHA");
        JButton growBodyButton = new JButton("GROW BODY");
        JButton disperseSporeButton = new JButton("DISPERSE SPORE");
        JButton eatInsectButton = new JButton("EAT INSECT");

        //Buggerhez kötődő gombok
        JButton moveButton = new JButton("MOVE");
        JButton cutHyphaButton = new JButton("CUT HYPHA");
        JButton eatSporeButton = new JButton("EAT SPORE");

        mushroomButtonList.add(growHyphaButton);
        mushroomButtonList.add(growBodyButton);
        mushroomButtonList.add(disperseSporeButton);
        mushroomButtonList.add(eatInsectButton);

        buggerButtonList.add(moveButton);
        buggerButtonList.add(cutHyphaButton);
        buggerButtonList.add(eatSporeButton);

        //MUSHROOMER gombjai
        growHyphaButton.setBounds(100,50,100,20);
        growHyphaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                state = State.growHypha;
                stateChanged();
            }
        });
        sidePanel.add(growHyphaButton,BorderLayout.NORTH);

        growBodyButton.setBounds(100,50,100,20);
        growBodyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                state = State.growBody;
                stateChanged();
            }
        });
        sidePanel.add(growBodyButton,BorderLayout.CENTER);

        disperseSporeButton.setBounds(100,50,100,20);
        disperseSporeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                state = State.disperseSpore;
                stateChanged();
            }
        });
        sidePanel.add(disperseSporeButton,BorderLayout.SOUTH);

        eatInsectButton.setBounds(100,50,100,20);
        eatInsectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                state = State.eatInsect;
                stateChanged();
            }
        });
        sidePanel.add(eatInsectButton,BorderLayout.SOUTH);

        //ROVARÁSZ gombjai
        moveButton.setBounds(100,50,150,20);
        moveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                state = State.move;
                stateChanged();
            }
        });
        sidePanel.add(moveButton,BorderLayout.NORTH);

        cutHyphaButton.setBounds(100,50,150,20);
        cutHyphaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                state = State.cutHypha;
                stateChanged();
            }
        });
        sidePanel.add(cutHyphaButton,BorderLayout.CENTER);

        eatSporeButton.setBounds(100,50,150,20);
        eatSporeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                state = State.eatSpore;
                stateChanged();
            }
        });
        sidePanel.add(eatSporeButton,BorderLayout.SOUTH);

        //Exec button
        executeButton = new JButton("EXECUTE");
        executeButton.setEnabled(false);
        executeButton.setVisible(false);

        executeButton.setBounds(100,50,100,20);
        executeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switch(state){
                    case growHypha: {
                        boolean check = controller.connectButtonPressed((String)fromComboBox.getSelectedItem(),
                                (String)toComboBox.getSelectedItem(),
                                ((String)thirdoptionComboBox.getSelectedItem()).equals("-") ? "" : (String)thirdoptionComboBox.getSelectedItem(),
                                currentPlayer);
                        if(check){
                            updatePlayer();
                        }
                        stateChanged();
                        break;
                    }
                    case growBody:{
                        boolean check = controller.growButtonPressed((String)toComboBox.getSelectedItem(), currentPlayer);
                        if(check){
                            updatePlayer();
                        }
                    }
                    case disperseSpore:{
                        boolean check = controller.disperseButtonPressed((String)fromComboBox.getSelectedItem(), (String)toComboBox.getSelectedItem(), currentPlayer);
                        if(check){
                            updatePlayer();
                        }
                        break;
                    }
                    case eatInsect:{
                        boolean check = controller.eat_insectButtonPressed((String)insectComboBox.getSelectedItem(), currentPlayer);
                        if(check){
                            updatePlayer();
                        }
                        break;
                    }
                    case move:{
                        boolean check = controller.moveButtonPressed((String)fromComboBox.getSelectedItem(), (String)toComboBox.getSelectedItem(), (String)insectComboBox.getSelectedItem());
                        if(check){
                            updatePlayer();
                        }
                        break;
                    }
                    case cutHypha:{
                        boolean check = controller.cutButtonPressed((String)fromComboBox.getSelectedItem(), (String)toComboBox.getSelectedItem(), (String)insectComboBox.getSelectedItem());
                        if(check){
                            updatePlayer();
                        }
                        break;
                    }
                    case eatSpore:{
                        boolean check = controller.eat_sporeButtonPressed((String)insectComboBox.getSelectedItem());
                        if(check){
                            updatePlayer();
                        }
                        break;
                    }
                }
                stateChanged();
            }
        });

        fromComboBox = new JComboBox();
        fromComboBox.setEnabled(false);
        fromComboBox.setVisible(false);

        toComboBox = new JComboBox();
        toComboBox.setEnabled(false);
        toComboBox.setVisible(false);

        thirdoptionComboBox = new JComboBox();
        thirdoptionComboBox.setEnabled(false);
        thirdoptionComboBox.setVisible(false);


        insectComboBox = new JComboBox();
        insectComboBox.setEnabled(false);
        insectComboBox.setVisible(false);


        bottomRightPanel.add(executeButton,BorderLayout.NORTH);
        bottomRightPanel.add(fromComboBox,BorderLayout.WEST);
        bottomRightPanel.add(thirdoptionComboBox,BorderLayout.SOUTH);
        bottomRightPanel.add(toComboBox,BorderLayout.EAST);



        bottomRightPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);

        // "from" label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        fromLabel = new JLabel("From");
        fromLabel.setFont(new Font("Arial", Font.BOLD, 14));
        fromLabel.setVisible(false);
        bottomRightPanel.add(fromLabel, gbc);

        // "to" label
        gbc.gridx = 1;
        toLabel = new JLabel("To");
        toLabel.setFont(new Font("Arial", Font.BOLD, 14));
        toLabel.setVisible(false);
        bottomRightPanel.add(toLabel, gbc);

        // "thirdoption" label
        gbc.gridx = 2;
        thirdoptionLabel = new JLabel("Thirdoption");
        thirdoptionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        thirdoptionLabel.setVisible(false);
        bottomRightPanel.add(thirdoptionLabel, gbc);

        // "insect" label
        gbc.gridx = 2;
        iLabel = new JLabel("Insect");
        iLabel.setFont(new Font("Arial", Font.BOLD, 14));
        iLabel.setVisible(false);
        bottomRightPanel.add(iLabel, gbc);

        // "from" combo box
        gbc.gridx = 0;
        gbc.gridy = 1;
        bottomRightPanel.add(fromComboBox, gbc);

        // "to" combo box
        gbc.gridx = 1;
        bottomRightPanel.add(toComboBox, gbc);

        // "thirdoption" combo box
        gbc.gridx = 2;
        bottomRightPanel.add(thirdoptionComboBox, gbc);

        // "insect" combo box
        gbc.gridx = 2;
        bottomRightPanel.add(insectComboBox, gbc);

        // Button (spans two columns)
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        bottomRightPanel.add(executeButton, gbc);

        // Startgame
        insectPlayerLabel = new JLabel("Insects");
        labelList.add(insectPlayerLabel);
        insectPlayerLabel.setBounds(0,25,100,20);
        sidePanel.add(insectPlayerLabel);

        insectPlayerComboBox = new JComboBox<>();
        sidePanel.add(insectPlayerComboBox);

        mushroomPlayerLabel = new JLabel("Mushrooms");
        labelList.add(mushroomPlayerLabel);
        mushroomPlayerLabel.setBounds(25,50,100,20);
        sidePanel.add(mushroomPlayerLabel);

        mushroomPlayerComboBox = new JComboBox<>();
        sidePanel.add(mushroomPlayerComboBox);

        for (int i = 1; i <= 3; i++) {
            insectPlayerComboBox.addItem(i);
            mushroomPlayerComboBox.addItem(i);
        }

        startGameButton = new JButton("START");
        startGameButton.setBounds(225,50,150,20);
        startGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                state = State.statelessbugger;
                stateChanged();
                Model.clearMap();
                Model.start((int) mushroomPlayerComboBox.getSelectedItem(), (int) insectPlayerComboBox.getSelectedItem());
                View.clearView();
                Model.init();
                updatePlayer();
            }
        });
        sidePanel.add(startGameButton);

        frame.add(sidePanel);
        frame.add(bottomRightPanel);
        frame.setVisible(true);
        stateChanged();

    }
    /*
    * Azok a fv-ek, amik az objektumView-eket listába rakják
     */
    public static void addTView(TectonView t) {
        tectonViewList.add(t);
        refreshMap();
    }
    public static void addSView(SporeView s) {
        sporeViewList.add(s);
        refreshMap();
    }
    public static void addIView(InsectView i) {
        insectViewList.add(i);
        refreshMap();
    }
    public static void addINGView(IngrownHyphaView h) {
        ingViewList.add(h);
        refreshMap();
    }
    public static void addINTView(InterconnectingHyphaView h) {
        interViewList.add(h);
        refreshMap();
    }
    public static void addBView(MushroomBodyView b) {
        bodyViewList.add(b);
        refreshMap();
    }
    public static void removeSView(SporeView s) {
        sporeViewList.remove(s);
        refreshMap();
    }
    public static void removeInsectView(InsectView s) {
        insectViewList.remove(s);
        refreshMap();
    }
    public static void removeInterconnectingHyphaView(InterconnectingHyphaView s) { interViewList.remove(s); refreshMap();}
    public static void removeIngrownHyphaView(IngrownHyphaView s) { ingViewList.remove(s); refreshMap();}
    /*
     * getter függvény
     */
    public static TectonView getT(Tecton t){
        for(TectonView v: tectonViewList){
            if(v.getTecton().equals(t)){
                return v;
            }
        }
        return null;
    }
    /*
     * getter függvény
     */
    public static InterconnectingHyphaView getICH(InterconnectingHypha h){
        for(InterconnectingHyphaView v: interViewList){
            if(v.getInterconnectingHypha().equals(h)){
                return v;
            }
        }
        return null;
    }
    /*
     * getter függvény
     */
    public static IngrownHyphaView getING(IngrownHypha h){
        for(IngrownHyphaView v: ingViewList){
            if(v.getIngrownHypha().equals(h)){
                return v;
            }
        }
        return null;
    }
    /*
     * getter függvény
     */
    public static SporeView getS(Spore s){
        for(SporeView v: sporeViewList){
            if(v.getSpore().equals(s)){
                return v;
            }
        }
        return null;
    }
    /*
     * getter függvény
     */
    public static InsectView getI(Insect i){
        for(InsectView v: insectViewList){
            if(v.getInsect().equals(i)){
                return v;
            }
        }
        return null;
    }
 /*
 * Frissíti a térkép állását
  */
    public static void refreshMap() {
        if(!canvas.isRepainted())
            canvas.paint(canvas.getGraphics());
        canvas.setRepainted(false);
    }
    /*
    * A jelenegi állapot alapján frissíti az elemek láthatóságát
     */
    public static void stateChanged(){
        switch(state){
            case growHypha: {
                disapperingComboboxes();
                fromComboBox.removeAllItems();
                for(String name : Model.getTectonNames()){
                    fromComboBox.addItem(name);
                }
                fromComboBox.setEnabled(true);
                fromComboBox.setVisible(true);
                fromLabel.setVisible(true);

                toComboBox.removeAllItems();
                for(String name : Model.getTectonNames()){
                    toComboBox.addItem(name);
                }
                toComboBox.setEnabled(true);
                toComboBox.setVisible(true);
                toLabel.setVisible(true);

                thirdoptionComboBox.removeAllItems();
                thirdoptionComboBox.addItem("-");
                for(String name : Model.getTectonNames()){
                    thirdoptionComboBox.addItem(name);
                }
                thirdoptionComboBox.setEnabled(true);
                thirdoptionComboBox.setVisible(true);
                thirdoptionLabel.setVisible(true);

                executeButton.setVisible(true);
                executeButton.setEnabled(true);
                break;
            }
            case disperseSpore:{
                disapperingComboboxes();
                fromComboBox.removeAllItems();
                for(String name : Model.getTectonNames()){
                    fromComboBox.addItem(name);
                }
                fromComboBox.setEnabled(true);
                fromComboBox.setVisible(true);
                fromLabel.setVisible(true);

                toComboBox.removeAllItems();
                for(String name : Model.getTectonNames()){
                    toComboBox.addItem(name);
                }
                toComboBox.setEnabled(true);
                toComboBox.setVisible(true);
                toLabel.setVisible(true);

                executeButton.setVisible(true);
                executeButton.setEnabled(true);
                break;
            }
            case growBody:{
                disapperingComboboxes();
                toComboBox.removeAllItems();
                for(String name : Model.getTectonNames()){
                    toComboBox.addItem(name);
                }
                toComboBox.setEnabled(true);
                toComboBox.setVisible(true);
                toLabel.setVisible(true);
                executeButton.setVisible(true);
                executeButton.setEnabled(true);
                break;
            }
            case eatInsect, eatSpore:{
                disapperingComboboxes();
                insectComboBox.removeAllItems();
                for(String name : Model.getInsectNames()){
                    insectComboBox.addItem(name);
                }
                insectComboBox.setEnabled(true);
                insectComboBox.setVisible(true);
                iLabel.setVisible(true);
                executeButton.setVisible(true);
                executeButton.setEnabled(true);
                break;
            }
            case move, cutHypha:{
                disapperingComboboxes();
                fromComboBox.removeAllItems();
                for(String name : Model.getTectonNames()){
                    fromComboBox.addItem(name);
                }
                fromComboBox.setEnabled(true);
                fromComboBox.setVisible(true);
                fromLabel.setVisible(true);
                toComboBox.removeAllItems();
                for(String name : Model.getTectonNames()){
                    toComboBox.addItem(name);
                }
                toComboBox.setEnabled(true);
                toComboBox.setVisible(true);
                toLabel.setVisible(true);
                executeButton.setVisible(true);
                executeButton.setEnabled(true);
                insectComboBox.removeAllItems();
                for(String name : Model.getInsectNames()){
                    insectComboBox.addItem(name);
                }
                insectComboBox.setEnabled(true);
                insectComboBox.setVisible(true);
                iLabel.setVisible(true);

                break;
            }
            case statelessmushroomer:{
                disapperingComboboxes();
                executeButton.setEnabled(false);
                executeButton.setVisible(false);
                toComboBox.setVisible(false);
                fromComboBox.setVisible(false);
                thirdoptionComboBox.setVisible(false);
                toLabel.setVisible(false);
                fromLabel.setVisible(false);
                thirdoptionLabel.setVisible(false);
                iLabel.setVisible(false);
                insectComboBox.setVisible(false);
                for(JButton m: mushroomButtonList){
                    m.setEnabled(true);
                    m.setVisible(true);
                }
                for(JButton b: buggerButtonList){
                    b.setEnabled(false);
                    b.setVisible(false);
                }

                insectPlayerLabel.setVisible(false);
                mushroomPlayerLabel.setVisible(false);
                insectPlayerComboBox.setEnabled(false);
                insectPlayerComboBox.setVisible(false);
                mushroomPlayerComboBox.setEnabled(false);
                mushroomPlayerComboBox.setVisible(false);
                startGameButton.setVisible(false);
                playerLabel.setVisible(true);

                scoreLabel.setText("Score: "+ Model.getPlayerScore(currentPlayer));

                break;
            }
            case statelessbugger:{
                disapperingComboboxes();
                executeButton.setEnabled(false);
                executeButton.setVisible(false);
                toComboBox.setVisible(false);
                fromComboBox.setVisible(false);
                thirdoptionComboBox.setVisible(false);
                toLabel.setVisible(false);
                fromLabel.setVisible(false);
                thirdoptionLabel.setVisible(false);
                iLabel.setVisible(false);
                insectComboBox.setVisible(false);
                for(JButton b: buggerButtonList){
                    b.setEnabled(true);
                    b.setVisible(true);
                    b.updateUI();
                }
                for(JButton m: mushroomButtonList){
                    m.setEnabled(false);
                    m.setVisible(false);
                    m.updateUI();
                }

                insectPlayerLabel.setVisible(false);
                mushroomPlayerLabel.setVisible(false);
                insectPlayerComboBox.setEnabled(false);
                insectPlayerComboBox.setVisible(false);
                mushroomPlayerComboBox.setEnabled(false);
                mushroomPlayerComboBox.setVisible(false);
                startGameButton.setVisible(false);
                playerLabel.setVisible(true);

                scoreLabel.setText("Score: "+ Model.getPlayerScore(currentPlayer));

                break;
            }
            case startgame: {
                disapperingComboboxes();
                executeButton.setEnabled(false);
                executeButton.setVisible(false);
                toComboBox.setVisible(false);
                fromComboBox.setVisible(false);
                toLabel.setVisible(false);
                fromLabel.setVisible(false);
                iLabel.setVisible(false);
                insectComboBox.setVisible(false);
                playerLabel.setVisible(false);

                for(JButton b: buggerButtonList){
                    b.setEnabled(false);
                    b.setVisible(false);
                    b.updateUI();
                }
                for(JButton m: mushroomButtonList){
                    m.setEnabled(false);
                    m.setVisible(false);
                    m.updateUI();
                }

                insectPlayerLabel.setVisible(true);
                mushroomPlayerLabel.setVisible(true);
                insectPlayerComboBox.setEnabled(true);
                insectPlayerComboBox.setVisible(true);
                mushroomPlayerComboBox.setEnabled(true);
                mushroomPlayerComboBox.setVisible(true);
                startGameButton.setVisible(true);

                break;
            }

        }
        refreshMap();
        menuPanel.updateUI();
        scoreLabel.updateUI();
    }

    /*
    * létrehozza a listákat
     */
    public static void clearView() {
        tectonViewList = new ArrayList<>();
        sporeViewList = new ArrayList<>();
        insectViewList = new ArrayList<>();
        interViewList = new ArrayList<>();
        ingViewList = new ArrayList<>();
        bodyViewList = new ArrayList<>();
    }

    /*
    * a comboboxok láthatóságát falsera állítja
     */
    public static void disapperingComboboxes(){
        fromComboBox.setVisible(false);
        toComboBox.setVisible(false);
        thirdoptionComboBox.setVisible(false);
        insectComboBox.setVisible(false);
        fromLabel.setVisible(false);
        toLabel.setVisible(false);
        thirdoptionLabel.setVisible(false);
        iLabel.setVisible(false);
    }
    /*
     * Insect győzelmet ellenőriz
     */
    public static boolean checkInsectVictoryAndShowWindow(int score) {
        if(score == Model.getInsectWinPoint()) {
            WINdow();
            return true;
        }
        return false;
    }

    /*
    * Mushroom győzelmet ellenőriz
     */
    public static boolean checkMushroomVictoryAndShowWindow(int score) {
        if(score == Model.getMushroomWinPoint()) {
            WINdow();
            return true;
        }
        return false;
    }

    /*
    * játék végi győzelem értesítésére szolgáló fv
     */
    private static void WINdow(){
        JFrame victoryWindow = new JFrame("Victory");

        JTextArea winTextArea = new JTextArea("END OF THE GAME!\n\n\n" +
                "YOU ARE VICTORIOUS" +
                "\n\n\n\n\n");

        winTextArea.setEditable(false);
        winTextArea.setBackground(Color.YELLOW);
        winTextArea.setForeground(Color.BLACK);
        winTextArea.setFont(new Font("Arial", Font.BOLD, 24)); // Opcionális: szebb betűtípus

        victoryWindow.add(winTextArea);
        victoryWindow.setSize(575, 700);
        victoryWindow.setResizable(false);
        victoryWindow.setLocationRelativeTo(null);
        victoryWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        victoryWindow.setVisible(true);
    }
}
