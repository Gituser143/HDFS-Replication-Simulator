package hdfs.replicationsimulator;

public class Power {
  public int writeHdd;
  public int readHdd;

  public int writeSsd;
  public int readSsd;

  public int ssdActive;
  public int hddActive;
  public int hddSleep;

  public int hddTransition;

  public int totalPower;

  public Power()
  {
    this.readHdd = 6;
    this.writeHdd = 6;

    this.readSsd = 2;
    this.writeSsd = 2;

    this.ssdActive = 8;
    this.hddActive = 17;
    this.hddSleep = 1;

    this.hddTransition = 10;

    this.totalPower = 0;
  }
}
