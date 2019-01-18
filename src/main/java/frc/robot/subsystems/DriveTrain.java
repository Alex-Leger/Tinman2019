/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.TankDrive;
import frc.robot.commands.Test;

/**
 * An example subsystem. You can replace me with your own Subsystem.
 */
public class DriveTrain extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  private WPI_TalonSRX rearLeft = new WPI_TalonSRX(RobotMap.rearLeft);
  private WPI_TalonSRX rearRight = new WPI_TalonSRX(RobotMap.rearRight);
  private WPI_TalonSRX frontLeft = new WPI_TalonSRX(RobotMap.frontLeft);
  private WPI_TalonSRX frontRight = new WPI_TalonSRX(RobotMap.frontRight);
  private Solenoid liftFront;
  private Solenoid liftBack;
  private AHRS ahrs = new AHRS(SPI.Port.kMXP);

  public DriveTrain() {
    super();
    liftFront = new Solenoid(RobotMap.LiftFront);
    liftBack = new Solenoid(RobotMap.LiftBack);

    rearLeft.set(ControlMode.Follower, frontLeft.getDeviceID());
    rearRight.set(ControlMode.Follower, frontRight.getDeviceID());
  }

  public void drive(double leftSpeed, double rightSpeed) {
    frontLeft.set(-leftSpeed);
    frontRight.set(rightSpeed);
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
    //setDefaultCommand(new TankDrive());
    setDefaultCommand(new Test());
  }

  public double getAngle() {
    return ahrs.getAngle();
  }

  public void ResetGyro() {
    ahrs.reset();
  }

  public void LiftRobot() {
    liftFront.set(true);
    liftBack.set(true);
  }

  public void LowerRobot() {
    liftFront.set(false);
    liftBack.set(false);
  }
}