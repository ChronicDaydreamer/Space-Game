import java.awt.Graphics;
import java.awt.Color;
import java.util.Arrays;
import java.util.Random;
import java.util.LinkedList;
public class StarSystem extends GameObject{
    Main game;
    double mass;
    double radius;
    double lumin;
    double temp;
    double lifetime;
    double density;
    double age;
    String type;
    String subtype;
    Color color;
    double[] habZone;
    double frostLine;
    LinkedList<Planet> planetList= new LinkedList<Planet>();
    double[] stableOrbs = new double[10];
    double distance=0.1;
    double metal=1;
    Random r;
    public int rX,rY;
    public int sX,sY;



//sets all the info and generates star characteristics, the potential orbits of the system, and finally the planets themselves
    public StarSystem(int x, int y, ID id,int num, int massNum, double lifePercent,Random r, Main game){
      super(x,y,id);
      this.game=game;
      this.r=r;
      this.rX=x-game.startX;
      this.rY=y-game.startY;
      typeGen(num, massNum, lifePercent);
      orbitGen(Math.pow(mass,(1/3))*0.22);
      planetGen();
      //System.out.println("The frostline of this system is located at "+this.frostLine);
    }

    public void tick(){
    }
    public void render(Graphics g){
      g.setColor(Color.black);
      g.fillRect(0,0,game.WIDTH,game.HEIGHT);
      g.setColor(color);
      g.fillOval((int)((game.WIDTH/2)-(42*radius)/2),(int)((game.HEIGHT/2)-(42*radius)/2),(int)(42*radius),(int)(42*radius));
      g.setColor(Color.white);
      g.drawOval((int)((game.WIDTH/2)-this.frostLine), (int)((game.HEIGHT/2)-this.frostLine),(int)(this.frostLine*2),(int)(this.frostLine*2));
    }
    //randomly picks a type of star based on the actual distribution of stellar types in the milky way, and generates info from that 
    public void typeGen(int num, int massNum, double lifePercent){
      if(num==0){
        this.type="exotic";
        this.mass=2;
        this.color=Color.magenta;
      }else if(num>0&&num<31){
        type="O";
        mass=(184/100)*massNum+16;
        color=Color.blue;
      }else if(num>30&&num<1231){
        type="B";
        mass=(13.9/100)*massNum+2.1;
        color=Color.cyan;
      }else if(num>1230&&num<7331){
        type="A";
        mass=(0.7/100)*massNum+1.4;
        color=Color.gray;
      }else if(num>7330&&num<37331){
        type="F";
        mass=(0.36/100)*massNum+1.04;
        color=Color.white;
      }else if(num>31330&&num<113331){
        this.type="G";
        this.mass=(0.24/100)*massNum+0.8;
        this.color=Color.yellow;
      }else if(num>113330&&num<233331){
        this.type="K";
        this.mass=(0.35/100)*massNum+0.45;
        this.color=Color.orange;
      }else{
        this.type="M";
        this.mass=(0.4/100)*massNum+0.05;
        this.color=Color.red;
      }
      mass=((double)(Math.round(mass*1000)))/1000;
      if(mass<1){
        radius=Math.pow(mass, 0.8);
      }else{
        radius=Math.pow(mass, 0.57);
      }
      density=mass/(Math.pow(radius,3));
      if(mass<0.43){
        lumin=Math.pow(mass, 2.3)*0.23;
      }else if(mass<2){
        lumin=Math.pow(mass, 4);
      }else{
        lumin=Math.pow(mass,3.5)*1.4;
      }
      temp=((int)Math.round(Math.pow(lumin/Math.pow(radius,2),0.25)*5776));
      lifetime=((double)(Math.round((mass/lumin)*1000))/100);
      age=((double)(Math.round(14*lifePercent*100)))/100;

    }
    //generates the potential orbits of the system based on the closest potential orbit and a spacing factor
    public void orbitGen(double spacing){
      int orbitNum=(int)((Math.pow((mass-1)*100, 1/3)+15)+(Math.log(mass+0.00012)-3));
      double[] newOrbits=new double[orbitNum];
      double innerLim=(double)(Math.round((2.455*(radius*696340)*Math.pow(((density*1408)/5400),(1/3))/149600000)*1000))/1000;
      newOrbits[0]=innerLim*10;
      for(int i=1;i<orbitNum;i++){
        newOrbits[i]=(double)(Math.round((newOrbits[i-1]+spacing*Math.pow(2,i-1))*1000))/1000;
      }
      stableOrbs=Arrays.copyOf(newOrbits, newOrbits.length);
      //System.out.println(orbitNum);
      for (double element: stableOrbs) {
            //System.out.println(element);
        }
      this.frostLine=4.85*Math.pow(this.lumin,0.5);
    }
    //determines if a potential orbit actually hosts a planet, and if so, generates that info
    public void planetGen(){
      int planetCounter=0;
      double chanceOfPlanet=(-(Math.log(planetCounter+1)/Math.log(2))+(Math.log(51)/Math.log(2)))/11;
      for(int i =0; i<stableOrbs.length;i++){
        if(r.nextInt(10000)<chanceOfPlanet*10000){
          Planet newPlanet=new Planet(this, game.WIDTH/2,game.HEIGHT/2,ID.Planet,stableOrbs[i],this.r);
          //System.out.println("There is a planet at "+newPlanet.orbitRadius+" au");
          this.planetList.add(newPlanet);
          planetCounter++;
        }
      }
      for(int i=0; i<this.planetList.size();i++){
        this.planetList.get(i).setDistanceFactor(((this.y))/this.planetList.get(planetCounter-1).getOrbitRadius());
      }
    }
}
