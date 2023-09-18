import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;
import java.util.Random;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.util.LinkedList;
public class Menu extends MouseAdapter{
  private Main game;
  private Handler handler;
  Random random = new Random();
  StarSystem currentSystem;

  public Menu(Main game, Handler handler){
    this.game=game;
    this.handler=handler;
    System.out.println("The seed is "+game.seed);
    random.setSeed(game.seed);
  }

  public void mousePressed(MouseEvent e){
    int mx=e.getX();
    int my=e.getY();
    if(mouseOver(mx,my,200,100,400,64)&&game.gameState==Main.STATE.Menu){
      game.gameState=Main.STATE.Generate;
      for(int i =0;i<game.systemList.length;i++){

        StarSystem s = new StarSystem(random.nextInt(Main.WIDTH),random.nextInt(Main.HEIGHT),ID.StarSystem,random.nextInt(1000000), random.nextInt(100), random.nextDouble(), random, game);
        game.systemList[i]=s;

      }
    }else if(game.gameState==Main.STATE.Generate&&mouseOver(mx,my,200,300,400,64)){
      game.gameState=Main.STATE.Map;


      //System.out.println("This is an "+s.type+" type star\n"+"Mass: "+s.mass+" Msol\n"+"Age: "+s.age+" Gyr\n"+"Temperature: "+s.temp+" Kelvin");
      //for(int i =0; i<s.planetList.size();i++){
      //  System.out.print("Planet " +i+" is a "+s.planetList.get(i).type+" planet, located at"+s.planetList.get(i).orbitRadius+" and has a mass of "+s.planetList.get(i).mass+"\n");
      //}

    }else if(game.gameState==Main.STATE.Map){
      for(int i =0; i<game.systemList.length;i++){
        if(mouseOver(mx,my,(int)(game.systemList[i].rX-10),(int)(game.systemList[i].rY-10),(int)(20*game.zoomFactor),(int)(20*game.zoomFactor))){
          handler.empty();
          currentSystem=game.systemList[i];
          double xChange=game.playerX-currentSystem.rX;
          double yChange=game.playerY-currentSystem.rY;
          game.resetCoords(xChange,yChange);
          System.out.println("changed new system at "+mx+" "+my);
          game.gameState=Main.STATE.Game;
          System.out.println("This is an "+currentSystem.type+" type star\n"+"Mass: "+currentSystem.mass+" Msol\n"+"Age: "+currentSystem.age+" Gyr\n"+"Temperature: "+currentSystem.temp+" Kelvin");
          handler.addObject(currentSystem);
          for(int j=0;j<currentSystem.planetList.size();j++){
            Planet tempPlanet=currentSystem.planetList.get(j);
            handler.addObject(tempPlanet);
            System.out.println("Planet "+j+":\n Distance:"+tempPlanet.orbitRadius+" Au \n Type: "+tempPlanet.type+"\n Mass: "+tempPlanet.mass);
          }
        }
      }

    }else if(game.gameState==Main.STATE.Game&&mouseOver(mx,my,50,50,100,40)){
      game.gameState=Main.STATE.Map;

    }
  }
  public void mouseReleased(MouseEvent e){

  }
  //for the buttons
  private boolean mouseOver(int mx, int my, int x, int y, int width, int height){
    if(mx>x&&mx<x+width){
      if(my>y&&my<y+height){
        return true;
      }else{
        return false;
      }
    }else{
      return false;
    }
  }

  public void tick(){

  }
  //for the gui, text, and stars/planets
  public void render(Graphics g){
    if(game.gameState==Main.STATE.Menu){
      Font fnt=new Font("arial",1,50);
      Font fnt2=new Font("arial",1,30);

      g.setFont(fnt);
      g.setColor(Color.white);
      g.drawString("Menu", 300,50);
      g.setFont(fnt2);

      //start button
      g.setColor(Color.white);
      g.drawRect(200,100,400,64);
      g.drawString("Play", 200,150);
      //options button
      g.setColor(Color.white);
      g.drawRect(200,200,400,64);
      g.drawString("Options", 200,250);
      //quit button
      g.setColor(Color.white);
      g.drawRect(200,300,400,64);
      g.drawString("Quit", 200,350);
    }else if(game.gameState==Main.STATE.Generate){
      g.setColor(Color.black);
      g.fillRect(0,0,game.WIDTH,game.HEIGHT);
      Font fnt=new Font("arial",1,50);
      Font fnt2=new Font("arial",1,30);
      g.setFont(fnt);
      g.setColor(Color.white);
      g.drawString("World Settings", 300,50);
      g.setFont(fnt2);

      //generate button
      g.setColor(Color.white);
      g.drawRect(200,300,400,64);
      g.drawString("Generate", 200,350);
    }else if(game.gameState==Main.STATE.Game){

      Font fnt=new Font("arial",1,50);
      Font fnt2=new Font("arial",1,30);
      g.setFont(fnt);
      g.setColor(Color.white);
      g.drawString("Game", 300,50);
      g.setFont(fnt2);

      //exit button
      g.setColor(Color.white);
      g.drawRect(50,50,100,40);
      g.drawString("exit", 75,75);
    }else if(game.gameState==Main.STATE.Map){
      g.setColor(Color.black);
      g.fillRect(0,0,game.WIDTH,game.HEIGHT);
      for(int i=0;i<game.systemList.length;i++){
        g.setColor(game.systemList[i].color);
        g.fillOval((int)(game.systemList[i].rX-10),(int)(game.systemList[i].rY-10),(int)(20*game.zoomFactor),(int)(20*game.zoomFactor));
      }
      g.setColor(Color.white);
      g.drawOval(game.WIDTH/2-13,game.HEIGHT/2-13,26,26);
    }
  }
}
