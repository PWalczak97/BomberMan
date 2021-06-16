package com.company;


import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main extends JPanel implements KeyListener{

    List<Sektor> sektory = new ArrayList<>();
    List<Sektor> sciany = new ArrayList<>();
    List<Soldier> wrogowie = new ArrayList<>();
    boolean menu = true;
    boolean zasady = true;
    int menuX = 300;
    int menuY = 150;
    int wyborPostaciX = 300;
    int wyborPostaciY = 150;
    int wyborBlokowX = 300;
    int wyborBlokowY = 245;
    int x = 0;
    int y = 0;
    int speed = 50;
    int numberOfWalls = 10;
    int pkt = 0;
    String typPostaci = "";
    String obraz = "bomberman-stay" + typPostaci + ".png";
    boolean plantBomb;
    boolean explosion;
    int explRange = 50;
    int bombX= -50;
    int bombY = -50;
    int bombTimer;
    boolean bonus = false;
    boolean bonus2 = false;
    boolean noMoreBonus = false;
    boolean noMoreBonus2 = false;
    int bonusX;
    int bonusY;
    int bonus2X;
    int bonus2Y;
    int soldierSpeed = 10;
    boolean gameOver = false;
    int blocked = 0;
    boolean bombSound = true;
    boolean gameOverSound = true;
    boolean nextRound = false;
    int nrRundy = 1;
    boolean control = false;

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000,500);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);



        rysujTlo(g);
        if(!menu&&!gameOver)
            rysujBomberMana(g);



        rysujSoldier(g);

        if(bonus){
            rysujBonus(g);
            if(x==bonusX&&y==bonusY){
               playSound("bonus-sound.wav",0);
                bonus = false;
                explRange = 100;
            }
        }
        if(bonus2){
            rysujBonus2(g);
            if(x==bonus2X&&y==bonus2Y){
                bonus2 = false;
                rysujEksplozjeBonusu(g);
            }


        }

        rysujSciane(g);

        if(plantBomb){
            if(bombSound){
               playSound("bomb-sound.wav",0);
                bombSound = false;
            }

            rysujBombe(g);
        }


        if(explosion){
            rysujEksplozje(g);
            explosion=false;
            bombX=-50;
            bombY=-50;
            bombSound = true;
        }


        if(bombTimer>0)
            bombTimer--;

        if(bombTimer==1){
            plantBomb = false;
            explosion = true;
        }

        if(menu)
            rysujMenu(g);



        if(gameOver){
            rysujGameOver(g);
            if(gameOverSound) {
               playSound("gameover-sound.wav",0);
            }
            gameOverSound = false;
        }

        if(nextRound){

                rysujNextRound(g);
                if(control){
                    nrRundy++;
                    playSound("round"+nrRundy+"-sound.wav",0);
                    System.out.println(nrRundy);
                    podzialNaSektory();
                    control=false;
                }

        }

    }

    private void rysujNextRound(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect((getWidth()/2)-200,200,400,100);
        g.setFont(new Font("TimesRoman",Font.BOLD,50));
        g.setColor(Color.BLACK);

        g.drawString("Round " + nrRundy,(getWidth()/2)-100,250);
        g.setFont(new Font("TimesRoman",Font.BOLD,15));
        g.drawString("Kliknij 'Space'",(getWidth()/2)-55,275);
    }

    public void playSound(String sound, float volume){
        AudioInputStream audioInputStream;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(sound)
                    .getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            FloatControl gainControl =
                    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(volume);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }



    private void rysujGameOver(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(250,150,500,200);
        g.setColor(Color.black);
        g.setFont(new Font("TimesRoman", Font.ITALIC,50));
        g.drawString("GAME OVER",350,225);
        g.drawString("Zdobyte pkt: " + pkt,340,290);
        g.setFont(new Font("Courier",Font.BOLD,20));
        g.drawString("Kliknij 'R' aby zrestartowac gre",360,330);

    }

    private void rysujMenu(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(250,0,500,500);

        if(zasady){
            g.setColor(Color.BLACK);
            g.setFont(new Font("Courier",Font.BOLD,20));
            g.drawString("Zasady:",270,95);
            g.drawString("Za zniszczenie bloku dostajesz 1pkt, za zabicie",265,125);
            g.drawString("żołnierza Twoje punkty są podwajane. Jeśli bomba",265,150);
            g.drawString("zniszczy kilka bloków, każdy kolejny jest warty o",265,175);
            g.drawString("1pkt wiecej. Masz 3 rundy aby zdobyc jak najwięcej ",265,200);
            g.drawString("punktow. Z bloków mogą wypaść bonusy: ",270,225);

            try {
                g.drawImage(ImageIO.read(new File("bomb-bonus.png")),350,240,100,100,null);
                g.drawImage(ImageIO.read(new File("range-bonus.png")),550,240,100,100,null);
            } catch (IOException e) {
                e.printStackTrace();
            }

            g.setFont(new Font("Courier",Font.BOLD,10));
            g.drawString("Zwieksza zasieg eksplozji",535,360);
            g.drawString("(wypada raz)",565,380);
            g.drawString("Niszczy wszystko w pionie i poziomie",317,360);
            g.drawString("z wyjątkiem centrum",360,380);
            g.drawString("(wypada raz)",370,400);
            g.setFont(new Font("Courier",Font.BOLD,20));
            g.drawString("Kliknij 'Space' aby kontynuować",360,475);

        } else {
            g.setColor(Color.WHITE);
            g.fillRect(wyborPostaciX,wyborPostaciY,50,50);
            g.fillRect(wyborBlokowX,wyborBlokowY,50,50);
            g.setColor(Color.BLACK);
            g.setFont(new Font("Courier",Font.BOLD,50));
            g.drawString("MENU",(getWidth()/2)-60,55);
            g.setFont(new Font("Courier",Font.BOLD,20));
            g.drawString("Kliknij 'Enter' aby wybrac",270, 100);
            g.drawString("Wybierz kolor postaci:",270, 130);

            try {
                g.drawImage(ImageIO.read(new File("bomberman-stay.png")),300,150,50,50,null);
                g.drawImage(ImageIO.read(new File("bomberman-stay-red.png")),400,150,50,50,null);
                g.drawImage(ImageIO.read(new File("bomberman-stay-blue.png")),500,150,50,50,null);
            } catch (IOException e) {
                e.printStackTrace();
            }

            g.drawString("Wybierz ilość bloków:",270,230);
            g.drawString("10",315,280);
            g.drawString("15",415,280);
            g.drawString("20",515,280);

            g.drawString("Kliknij 'Space' aby zagrać",390,475);
            g.setColor(Color.RED);
            g.drawRect(menuX,menuY,50,50);
        }

    }

    private void rysujBonus(Graphics g) {

        try {
            g.drawImage(ImageIO.read(new File("range-bonus.png")),bonusX,bonusY,50,50,null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void rysujBonus2(Graphics g) {
        try {
            g.drawImage(ImageIO.read(new File("bomb-bonus.png")),bonus2X,bonus2Y,50,50,null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void rysujEksplozjeBonusu(Graphics g) {

        int zasiegLewo = bonus2X/50;
        int zasiegPrawo = (getWidth()-bonus2X-50);
        int zasiegGora = bonus2Y/50;
        int zasiegDol = (getHeight()-bonus2Y-50);
        int korekta = 50;
        List<Sektor> eksplozja = new ArrayList<>();
        playSound("explosion-sound.wav",0);


        for(int i = 0;i<zasiegLewo;i++){
            try {
                g.drawImage(ImageIO.read(new File("explosion-lewo2.png")),bonus2X-korekta,bonus2Y,50,50,null);
                eksplozja.add(new Sektor(bonus2X-korekta,bonus2Y));


            } catch (IOException e) {
                e.printStackTrace();
            }
            korekta+=50;
        }
        korekta = 50;

        for(int i = 0;i<zasiegPrawo;i++){
            try {
                g.drawImage(ImageIO.read(new File("explosion-prawo2.png")),bonus2X+korekta,bonus2Y,50,50,null);
                eksplozja.add(new Sektor(bonus2X+korekta,bonus2Y));
            } catch (IOException e) {
                e.printStackTrace();
            }
            korekta+=50;
        }
        korekta = 50;

        for(int i = 0;i<zasiegDol;i++){
            try {
                g.drawImage(ImageIO.read(new File("explosion-dol2.png")),bonus2X,bonus2Y+korekta,50,50,null);
                eksplozja.add(new Sektor(bonus2X,bonus2Y+korekta));
            } catch (IOException e) {
                e.printStackTrace();
            }
            korekta+=50;
        }

        korekta = 50;

        for(int i = 0;i<zasiegGora;i++){
            try {
                g.drawImage(ImageIO.read(new File("explosion-gora2.png")),bonus2X,bonus2Y-korekta,50,50,null);
                eksplozja.add(new Sektor(bonus2X,bonus2Y-korekta));
            } catch (IOException e) {
                e.printStackTrace();
            }
            korekta+=50;
        }

        niszczenieScian(eksplozja);

        for(int i = wrogowie.size()-1;i>=0;i--)
        if(wrogowie.get(i).getY()==bonus2Y||(wrogowie.get(i).getX()+50>bonus2X&&wrogowie.get(i).getX()<bonus2X+50)){
            wrogowie.remove(i);
            pkt*=2;
        }


    }

    public void niszczenieScian(List<Sektor> eksplozje){
        int rand = (int)(Math.random()*5+1);
        int ilosc = 1;

        for(int i = 0;i<eksplozje.size();i++){
            for(int j = sciany.size()-1;j>=0;j--)
                if(sciany.get(j).getX()==eksplozje.get(i).getX()&&sciany.get(j).getY()==eksplozje.get(i).getY()){
                    if(rand==1&&!noMoreBonus){
                        bonus = true;
                        noMoreBonus = true;
                        bonusX = sciany.get(j).getX();
                        bonusY = sciany.get(j).getY();
                    } else if(rand==2&&!noMoreBonus2){
                        bonus2 = true;
                        noMoreBonus2 = true;
                        bonus2X = sciany.get(j).getX();
                        bonus2Y = sciany.get(j).getY();
                    }
                    sciany.remove(j);
                    pkt+=ilosc;
                    ilosc++;
                }

            if(eksplozje.get(i).getX()==x&&eksplozje.get(i).getY()==y)
                gameOver=true;

        }

        if(sciany.size()==0){
            if(nrRundy<3){
                nextRound=true;
                control = true;
                noMoreBonus=false;
                noMoreBonus2=false;
                explRange = 50;
                bonus = false;
                bonus2 = false;
                x=0;
                y=0;
                wrogowie.clear();
            } else
                gameOver=true;

        }
    }

    public void podzialNaSektory(){
        for(int i = 0; i<getWidth()/50; i++)
            for(int j = 0;j<getHeight()/50;j++)
            if((i==0&&j==0)||(i==0&&j==1)||(i==1&&j==0)||(i==1&&j==1)){

            }else{
                Sektor sektor = new Sektor(i*50,j*50);
                sektory.add(sektor);
            }

        Collections.shuffle(sektory);

        for(int i = 0;i<numberOfWalls;i++){
            sciany.add(sektory.get(i));
            sektory.remove(i);
        }

//      spawn zolnieza w pustym sektorze
        for(int i = 1;i<nrRundy;i++){
            wrogowie.add(new Soldier(sektory.get(i).getX(),sektory.get(i).getY(),
                    soldierSpeed,
                    "soldier-prawo" +
                    ".png"));
        }




//      Upewnienie sie aby zolnierz sie nie zablokowal

        for(int i =0;i<sciany.size();i++)
            for(int j =0; j<wrogowie.size();j++){
                if(((wrogowie.get(j).getY()==sciany.get(i).getY()&&(wrogowie.get(j).getX()-60<=sciany.get(i).getX())
                        &&wrogowie.get(j).getX()-40>=sciany.get(i).getX())) ||wrogowie.get(j).getX()==0){
                    blocked++;
                    wrogowie.get(j).setX(wrogowie.get(j).getX()+10);
                }
                else if(((wrogowie.get(j).getY()==sciany.get(i).getY()&&(wrogowie.get(j).getX()+60>=sciany.get(i)
                        .getX()) &&wrogowie.get(j).getX()+40<=sciany.get(i).getX())) ||wrogowie.get(j).getX() +50==getWidth()) {
                    blocked++;
                    wrogowie.get(j).setX(wrogowie.get(j).getX()-10);
                }

                if(blocked==2)
                    wrogowie.get(j).setSpeed(0);
            }


    }



    private void rysujSciane(Graphics g) {

        try {
            for(int i =0;i<sciany.size();i++)
                g.drawImage(ImageIO.read(new File("wall.png")), sciany.get(i).x, sciany.get(i).y, 50, 50, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void rysujSoldier(Graphics g) {
        try {
            for(int i = 0; i<wrogowie.size();i++)
            g.drawImage(ImageIO.read(new File(wrogowie.get(i).getObraz())),wrogowie.get(i).getX(),wrogowie.get(i).getY(),50,
                    50,
                    null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void rysujBombe(Graphics g) {

            try {
                g.drawImage(ImageIO.read(new File("bomberman-bomb.png")),bombX,bombY,50,50,null);
            } catch (IOException e) {
                e.printStackTrace();
            }


    }

    private void rysujTlo(Graphics g) {
        try {
            g.drawImage(ImageIO.read(new File("tlo.png")),0,0,getWidth(),getHeight(),null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void rysujBomberMana(Graphics g) {

        try {
            g.drawImage(ImageIO.read(new File(obraz)),x,y,50,50,null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean kolizjaZPrawej(int szer, int wys){

        for(int i = 0;i<sciany.size();i++)
            if((szer+50>=sciany.get(i).getX()&&szer+50<=sciany.get(i).getX()+50)&&wys==sciany.get(i).getY()||
                    (szer+50==bombX&&wys==bombY))
                return true;

        return false;
    }

    public boolean kolizjaZLewej(int szer, int wys){

        for(int i = 0;i<sciany.size();i++)
            if((szer==sciany.get(i).getX()+50&&wys==sciany.get(i).getY())||(szer==bombX+50&&wys==bombY))
                return true;

        return false;
    }

    public boolean kolizjaZGory(int szer, int wys){

        for(int i = 0;i<sciany.size();i++)
            if((szer==sciany.get(i).getX()&&wys+50==sciany.get(i).getY())||(szer==bombX&&wys+50==bombY))
                return true;

        return false;
    }

    public boolean kolizjaZDolu(int szer, int wys){

        for(int i = 0;i<sciany.size();i++)
            if((szer==sciany.get(i).getX()&&wys==sciany.get(i).getY()+50)||(szer==bombX&&wys==bombY+50))
                return true;

        return false;
    }

    private void rysujEksplozje(Graphics g) {

        List<Sektor> eksplozje = new ArrayList<>();
        playSound("explosion-sound.wav",-10f);


        try {
            if(explRange==50){
                g.drawImage(ImageIO.read(new File("explosion-mid.png")),bombX,bombY,50,50,null);
                g.drawImage(ImageIO.read(new File("explosion-prawo.png")),bombX+50,bombY,50,50,null);
                eksplozje.add(new Sektor(bombX+50,bombY));
                g.drawImage(ImageIO.read(new File("explosion-lewo.png")),bombX-50,bombY,50,50,null);
                eksplozje.add(new Sektor(bombX-50,bombY));
                g.drawImage(ImageIO.read(new File("explosion-dol.png")),bombX,bombY+50,50,50,null);
                eksplozje.add(new Sektor(bombX,bombY+50));
                g.drawImage(ImageIO.read(new File("explosion-gora.png")),bombX,bombY-50,50,50,null);
                eksplozje.add(new Sektor(bombX,bombY-50));
            } else {
                g.drawImage(ImageIO.read(new File("explosion-mid.png")),bombX,bombY,50,50,null);
                g.drawImage(ImageIO.read(new File("explosion-prawo2.png")),bombX+50,bombY,50,50,null);
                eksplozje.add(new Sektor(bombX+50,bombY));
                g.drawImage(ImageIO.read(new File("explosion-lewo2.png")),bombX-50,bombY,50,50,null);
                eksplozje.add(new Sektor(bombX-50,bombY));
                g.drawImage(ImageIO.read(new File("explosion-dol2.png")),bombX,bombY+50,50,50,null);
                eksplozje.add(new Sektor(bombX,bombY+50));
                g.drawImage(ImageIO.read(new File("explosion-gora2.png")),bombX,bombY-50,50,50,null);
                eksplozje.add(new Sektor(bombX,bombY-50));
                g.drawImage(ImageIO.read(new File("explosion-prawo.png")),bombX+100,bombY,50,50,null);
                eksplozje.add(new Sektor(bombX+100,bombY));
                g.drawImage(ImageIO.read(new File("explosion-lewo.png")),bombX-100,bombY,50,50,null);
                eksplozje.add(new Sektor(bombX-100,bombY));
                g.drawImage(ImageIO.read(new File("explosion-dol.png")),bombX,bombY+100,50,50,null);
                eksplozje.add(new Sektor(bombX,bombY+100));
                g.drawImage(ImageIO.read(new File("explosion-gora.png")),bombX,bombY-100,50,50,null);
                eksplozje.add(new Sektor(bombX,bombY-100));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        niszczenieScian(eksplozje);
        for(int i = wrogowie.size()-1;i>=0;i--)
        if((wrogowie.get(i).getY()==bombY&&(wrogowie.get(i)
                .getX()+50>bombX-explRange&&wrogowie.get(i).getX()<=bombX+50+explRange))||
                ((wrogowie.get(i).getY()+50>bombY-explRange&&wrogowie.get(i).getY()<bombY+50+explRange)&&
                        (wrogowie.get(i).getX()+50>bombX&&wrogowie.get(i).getX()<bombX+50))){
            wrogowie.remove(i);
            pkt*=2;
        }

        if(x==bombX&&y==bombY)
            gameOver = true;

    }


    public void animacja(){
         boolean bool = true;

         while(bool){

             if(!menu&&!gameOver&&!nextRound){
                 for(int i = 0; i<wrogowie.size();i++){
                     if(wrogowie.get(i).getX()+50>=getWidth()||kolizjaZPrawej(wrogowie.get(i).getX(),wrogowie
                             .get(i).getY())){
                        wrogowie.get(i).setSpeed(wrogowie.get(i).getSpeed()*(-1));
                        wrogowie.get(i).setObraz("soldier-lewo.png");
                     }
                     if(wrogowie.get(i).getX()<=0||kolizjaZLewej(wrogowie.get(i).getX(),wrogowie.get(i).getY())){
                         wrogowie.get(i).setSpeed(wrogowie.get(i).getSpeed()*(-1));
                         wrogowie.get(i).setObraz("soldier-prawo.png");
                     }
                     if((wrogowie.get(i).getY()==y&&(wrogowie.get(i).getX()+50==x||wrogowie.get(i).getX()-50==x)))
                         gameOver=true;
                     wrogowie.get(i).setX(wrogowie.get(i).getX()+wrogowie.get(i).getSpeed());
                 }
                 }



             try {
                 Thread.sleep(100);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
             repaint();
         }
    }



    public static void main(String[] args) {

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Main okno = new Main();
        window.add(okno);
        window.setVisible(true);
        window.addKeyListener(okno);
        window.pack();

        okno.animacja();

    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if(gameOver){

            if(key==KeyEvent.VK_R){
                gameOver=false;
                menu = true;
                x=0;
                y=0;
                pkt=0;
                bonus = false;
                bonus2 = false;
                noMoreBonus = false;
                noMoreBonus2 = false;
                explRange = 50;
                sektory.clear();
                sciany.clear();
                wrogowie.clear();
                blocked = 0;
                gameOverSound = true;
                nrRundy = 1;
            }


        }else if(zasady){

            if(key==KeyEvent.VK_SPACE){
                zasady = false;
                playSound("next-sound.wav",0);
            }




        } else if(menu){

            if(key==KeyEvent.VK_RIGHT&&menuX<=400){
                playSound("option-sound.wav",0);
                menuX+=100;
            }



            if(key==KeyEvent.VK_LEFT&&menuX>=400){
                playSound("option-sound.wav",0);

                menuX-=100;
            }



            if(key==KeyEvent.VK_DOWN){
                playSound("option-sound.wav",0);

                menuY=245;
            }



            if(key==KeyEvent.VK_UP){
                playSound("option-sound.wav",0);

                menuY=150;
            }



            if(key==KeyEvent.VK_ENTER&&menuY==150){
                playSound("accept-sound.wav",0);
                wyborPostaciX = menuX;
                wyborPostaciY = menuY;
                if(wyborPostaciX==300){
                    typPostaci = "";
                    obraz = "bomberman-stay" + typPostaci + ".png";
                }

                if(wyborPostaciX==400){
                    typPostaci = "-red";
                    obraz = "bomberman-stay" + typPostaci + ".png";
                }

                if(wyborPostaciX==500){
                    typPostaci = "-blue";
                    obraz = "bomberman-stay" + typPostaci + ".png";
                }
            }

            if(key==KeyEvent.VK_ENTER&&menuY==245){
                playSound("accept-sound.wav",0);
                wyborBlokowX = menuX;
                wyborBlokowY = menuY;

                if(wyborBlokowX==300)
                    numberOfWalls = 10;

                if(wyborBlokowX==400)
                    numberOfWalls = 15;

                if(wyborBlokowX==500)
                    numberOfWalls = 20;
            }

            if(key==KeyEvent.VK_SPACE) {
                playSound("next-sound.wav",-20f);
                menu = false;
                nextRound = true;
                playSound("round1-sound.wav",0);
                podzialNaSektory();
            }


        }  else if(nextRound){

            if(key==KeyEvent.VK_SPACE){
                nextRound=false;
            }
        }else{

            if((key==KeyEvent.VK_RIGHT&&x+50<getWidth())&&!kolizjaZPrawej(x,y)){
                x+=speed;
                obraz = "bomberman-prawo" + typPostaci +".png";
                playSound("footsteps-sound.wav",-10f);

            }


            if(key==KeyEvent.VK_LEFT&&x>0&&!kolizjaZLewej(x,y)){
                x-=speed;
                obraz = "bomberman-lewo"+typPostaci+".png";
                playSound("footsteps-sound.wav",-10f);
            }

            if(key==KeyEvent.VK_DOWN&&y+50<getHeight()&&!kolizjaZGory(x,y)){
                y+=speed;
                obraz = "bomberman-prawo"+typPostaci+".png";
                playSound("footsteps-sound.wav",-10f);
            }

            if(key==KeyEvent.VK_UP&&y>0&&!kolizjaZDolu(x,y)){
                y-=speed;
                obraz = "bomberman-lewo"+typPostaci+".png";
                playSound("footsteps-sound.wav",-10f);
            }

            if(key==KeyEvent.VK_SPACE&&!plantBomb){
                plantBomb = true;
                bombX = x;
                bombY = y;
                bombTimer=30;
            }
        }


        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if(key == KeyEvent.VK_RIGHT)
            obraz = "bomberman-stay"+typPostaci+".png";

        if(key == KeyEvent.VK_LEFT)
            obraz = "bomberman-stay"+typPostaci+".png";

        if(key == KeyEvent.VK_DOWN)
            obraz = "bomberman-stay"+typPostaci+".png";

        if(key == KeyEvent.VK_UP)
            obraz = "bomberman-stay"+typPostaci+".png";

        repaint();
    }
}
