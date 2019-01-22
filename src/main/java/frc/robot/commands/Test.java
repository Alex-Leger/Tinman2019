/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import frc.robot.auto_commands.DriveStraight;
import frc.robot.PixyPacket;
import frc.robot.Robot;
import frc.robot.subsystems.M_I2C;

/*public class Test extends CommandGroup{
  public Test() {
    addSequential(new DriveStraight(5,5));
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }
}*/
public class Test extends Command {
  M_I2C i2c = new M_I2C();// setup the i2c interface
  PixyPacket pkt = i2c.getPixy();// create a pixy packet to hold data

  public Test() {
    super();
    requires(Robot.m_drivetrain);

  }

  // public void centerOnObject() {
  @Override
  protected void initialize() {

    pkt = i2c.getPixy();
  }

  @Override
  protected void execute() {
    // System.out.println("here");
    pkt = i2c.getPixy();
    SmartDashboard.putNumber("x", pkt.x);
    SmartDashboard.putNumber("y", pkt.y);
    SmartDashboard.putNumber("area", pkt.area);
    if (pkt.x != -1) {// if data is exist
      
      pkt = i2c.getPixy();
      double p = 1;
      double error = 0.5 - pkt.x;
      double responce = p * error;
      Robot.m_drivetrain.drive(responce, 
      -responce);















      // if (pkt.x < .45 || pkt.x > .55) {
      // // and the 'object', whatever it may be is not in the center
      // // the code on the arduino decides what object to send
      // if (pkt.x < .45 || pkt.x > .55) {// while it is not center

      // if (pkt.x < .45) {// if its on the left side of robot, turn left
      // SmartDashboard.putString("direction", "left");
      // Robot.m_drivetrain.drive(.2, -.2);
      // pkt = i2c.getPixy();
      // }
      // if (pkt.x > .55) {// if its on the right side of robot, turn right
      // Robot.m_drivetrain.drive(-.2, .2);
      // SmartDashboard.putString("direction", "right");
      // pkt = i2c.getPixy();
      // }

      // if (pkt.x > .45 && pkt.x < .55) {// while centered, stop
      // Robot.m_drivetrain.drive(0, 0);
      // }
      // if (pkt.y == -1) {// Restart if ball lost during turn
      // Robot.m_drivetrain.drive(0, 0);

      // }
      // pkt = i2c.getPixy();// refresh the data
      // // System.out.println("XPos: " + pkt.x);// print the data just to see
      // }

      // }
    }
    pkt = i2c.getPixy();
  }

  @Override
  protected boolean isFinished() {
    return false;
  }
}