/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

/**
 * Runs the 4Bar Subsystem
 */
public class FourBarCheck extends Command {
    private boolean fourbardown = false;
    private boolean newPress = true;

    public FourBarCheck() {
        requires(Robot.m_fourbar);
    }

    @Override
    protected void execute() {
        if (Robot.m_oi.getLBumper()) {
            if(newPress){                     
                fourbardown = !fourbardown;
                newPress = false;
            }
        } 
        else
            newPress = true;
        
        Robot.m_fourbar.toggleFourBar(fourbardown);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
