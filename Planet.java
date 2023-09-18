import java.awt.Graphics;
import java.awt.Color;
import java.util.Random;
public class Planet extends GameObject{
  double mass;
  double density;
  double radius;
  double rotAngle;
  double orbitRadius; //in au
  double orbitTime; //in seconds
  double orbitSpeed;
  double timeInterval=1.0;
  double pixelDistance;
  double drawX;
  double drawY;
  int planetx=this.x;
  int planety=this.y;
  double distanceFactor;
  String type;
  Random r;
  StarSystem s;
  double renderedRadius;
  Color color;
  double frostLine;
  double[] atmosCompList=new double[24];
  //basically a setter for the planets orbit stuff, and generates the other info about the planet
  public Planet(StarSystem s, int x, int y, ID id,double newOrbitRadius, Random r){
    super(x,y,id);
    this.s=s;
    this.orbitRadius=newOrbitRadius;
    this.orbitTime=(Math.pow(Math.pow(this.orbitRadius,3)/this.s.mass,0.5))*365.24*24*60*60;
    this.orbitSpeed=2*Math.PI/orbitTime;
    this.r=r;
    this.orbitTime=this.orbitTime;
    genPlanet();
  }
  public void tick(){
    double radian=this.orbitSpeed*this.timeInterval;
    this.drawX=this.x+this.renderedRadius*Math.cos(radian);
    this.drawY=this.y+this.renderedRadius*Math.sin(radian);
    this.timeInterval+=0.01666666666*100000000;
  }
  public void render(Graphics g){
    if(this.renderedRadius>this.s.radius*21+this.mass*5){
      g.setColor(Color.gray);
      //System.out.println("the orbitradius at this time is "+this.orbitRadius);
      g.drawOval((int)(this.x-this.renderedRadius), (int)(this.y-this.renderedRadius),(int)(this.renderedRadius*2),(int)(this.renderedRadius*2));
      //System.out.println(this.x+" "+(this.x-this.orbitRadius));

      g.setColor(this.color);
      g.fillOval((int)(this.drawX-5*this.mass),(int)(this.drawY-5*this.mass),(int)(10*this.mass),(int)(10*this.mass));
    }
  }
  public double getOrbitRadius(){
    return this.orbitRadius;
  }
  //sets how zoomed in or out the camera is, not working rn
  public void setDistanceFactor(double distanceFactor){
    this.distanceFactor=distanceFactor;
    this.renderedRadius=this.orbitRadius*this.distanceFactor;
    this.frostLine=this.s.frostLine*this.distanceFactor;
    //System.out.println("The new orbit radius is "+this.renderedRadius);
  }
  //generates all the characteristics of the planet
  public void genPlanet(){
    double gasAfterFL=(3/(Math.pow((2*Math.PI),0.5)))*Math.pow(2.71828,(-0.5*Math.pow((this.orbitRadius-(this.s.frostLine/2)-12*this.s.frostLine)/(6*this.s.frostLine*this.s.metal),2)));
    double gasChance=(1.2/(Math.pow(2*Math.PI,0.5)))*Math.pow(2.71828,(-0.5*Math.pow((this.orbitRadius)/(this.s.metal*0.05),2)))+gasAfterFL;
    //System.out.println(gasChance);
    double randomDouble=r.nextDouble();
    //System.out.println(randomDouble);
    if(r.nextInt(1001)<gasChance*1000){

      this.type="gas";
      this.color=Color.pink;
      this.mass=5*(randomDouble)*((((3*this.s.mass*this.s.metal)/(Math.pow(2*Math.PI,0.5)))*Math.pow(2.71828,-0.5*Math.pow((this.orbitRadius)/(this.s.metal*this.s.mass),2)))+0.05);
      this.mass=this.mass*3*this.s.metal;
    }else{
      this.type="rocky";
      this.color=Color.cyan;
      this.mass=(3*randomDouble*this.s.metal)*(((this.s.mass*this.s.metal*this.s.frostLine)/Math.pow(2*Math.PI,0.5))*(Math.pow(2.71828,-0.5*Math.pow((this.orbitRadius-(this.s.frostLine/2))/(this.s.metal*this.s.mass),2)))+(this.s.metal/2)+(2.455)*(this.s.radius*696340)*(Math.pow((this.s.density*1408)/5400,(1/3))/149600000));
    }
    //System.out.println("the mass of this planet is "+this.mass);
  }
}
