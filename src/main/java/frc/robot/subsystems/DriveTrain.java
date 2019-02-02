/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.TankDrive;
import frc.robot.RobotSettingsFactory;

/**
 * It's the thing that makes the robot go vroom
 */
public class DriveTrain extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  private WPI_TalonSRX rearLeft;
  private WPI_TalonSRX rearRight;
  private WPI_TalonSRX frontLeft;
  private WPI_TalonSRX frontRight;
  private Solenoid liftFront;
  private Solenoid liftBack;
  private AHRS ahrs = new AHRS(SPI.Port.kMXP);
  private int oldLeft = 0;
  private int oldRight = 0;
  public boolean IsInit = false;

  public DriveTrain() {
    super();

    liftFront = new Solenoid(RobotMap.LiftFront);
    liftBack = new Solenoid(RobotMap.LiftBack);

  }

  public void initialize() {

    frontRight = new WPI_TalonSRX(RobotSettingsFactory.getRobotSettings(Robot.getCurrentBot()).getM_frontRight());
    frontLeft = new WPI_TalonSRX(RobotSettingsFactory.getRobotSettings(Robot.getCurrentBot()).getM_frontLeft());
    rearRight = new WPI_TalonSRX(RobotSettingsFactory.getRobotSettings(Robot.getCurrentBot()).getM_rearRight());
    rearLeft = new WPI_TalonSRX(RobotSettingsFactory.getRobotSettings(Robot.getCurrentBot()).getM_rearLeft());

    frontLeft.setSelectedSensorPosition(0, 0, 10);
    frontRight.setSelectedSensorPosition(0, 0, 10);
    rearLeft.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
    frontRight.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);

    rearLeft.set(ControlMode.Follower, frontLeft.getDeviceID());
    rearRight.set(ControlMode.Follower, frontRight.getDeviceID());
    IsInit = true;
  }

  public void drive(double leftSpeed, double rightSpeed) {
    frontLeft.set(-leftSpeed);
    frontRight.set(rightSpeed);
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
    setDefaultCommand(new TankDrive());

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

  public double getPosLeft() {
    return -rearLeft.getSensorCollection().getQuadraturePosition() - oldLeft;
  }

  public double getPosRight() {
    return frontRight.getSensorCollection().getQuadraturePosition() - oldRight;
  }

  public void getEncoders() {
    getPosLeft();
    getPosRight();

    SmartDashboard.putNumber("EncoderLeft", Robot.m_drivetrain.getPosLeft());
    SmartDashboard.putNumber("EncoderRight", Robot.m_drivetrain.getPosRight());

  }

  public void resetEncoders() {
    oldRight = frontRight.getSensorCollection().getQuadraturePosition();
    oldLeft = -rearLeft.getSensorCollection().getQuadraturePosition();
  }

}