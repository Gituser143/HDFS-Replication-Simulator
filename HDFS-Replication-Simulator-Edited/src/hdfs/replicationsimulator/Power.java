package hdfs.replicationsimulator;

public class Power {
  public int writeHdd;
  public int readHdd;

  public int writeSsd;
  public int readSsd;
  public int totalPower;
  public Power()
  {
    this.readHdd = 6;
    this.writeHdd = 6;

    this.readSsd = 2;
    this.writeSsd = 2;

    this.totalPower = 0;
  }
}
