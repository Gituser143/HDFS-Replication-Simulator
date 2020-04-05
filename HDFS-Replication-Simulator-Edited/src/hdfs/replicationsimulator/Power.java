package hdfs.replicationsimulator;

public class Power {

  public int bootSsd;
  public int bootHdd;

  public int writeHdd;
  public int readHdd;

  public int writeSsd;
  public int readSsd;

  public int ssdActive;
  public int hddActive;

  public int hddSleep;
  public int ssdSleep;

  public int hddTransition;

  public int totalPower;

  public Power()
  {

    this.bootHdd = 7;
    this.bootSsd = 4; //4

    this.readHdd = 6;
    this.writeHdd = 6;

    this.readSsd = 2; //2
    this.writeSsd = 2; //2

    this.ssdActive = 8; //8
    this.hddActive = 17; //17

    this.hddSleep = 2; //2
    this.ssdSleep = 1; //1

    this.hddTransition = 5; //5

    this.totalPower = 0;
  }
}
