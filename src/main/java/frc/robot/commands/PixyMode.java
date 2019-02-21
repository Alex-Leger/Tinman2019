/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.PixyPacket;
import frc.robot.Robot;
import frc.robot.subsystems.M_I2C2;;

public class PixyMode extends Command {
  M_I2C2 i2c = new M_I2C2();// setup the i2c interface
  PixyPacket pkt = i2c.getPixy();// create a pixy packet to hold data
  private double center, error, responce, derivative, errorPrior;
  private double p = .004;
  private double d = 0;

  public PixyMode() {
    super();
    requires(Robot.m_drivetrain);

  }

  @Override
  protected void initialize() {

  }

  @Override
  protected void execute() {
    pkt = i2c.getPixy();
    SmartDashboard.putNumber("error", pkt.error);
    if (pkt.error != -9999) {// if data is exist
      SmartDashboard.putString("Turning", "yes");

      error = pkt.error;
      derivative = (error - errorPrior);
      responce = p * error + (d * derivative);
      if (responce < -.5) {
        responce = -.5;
      } else if (responce > .5) {
        responce = .5;
      }
      Robot.m_drivetrain.drive(responce + Robot.m_oi.getLeftStick().getY(),
          -responce + Robot.m_oi.getLeftStick().getY());
      SmartDashboard.putNumber("error", error);
      SmartDashboard.putNumber("center", center);
      SmartDashboard.putNumber("responce", responce);
      errorPrior = error;

    } else {
      Robot.m_drivetrain.drive(0, 0);
      SmartDashboard.putNumber("center", 0);
      SmartDashboard.putString("Turning", "no");

    }
  }

  @Override
  protected boolean isFinished() {
    return false;
  }
}
