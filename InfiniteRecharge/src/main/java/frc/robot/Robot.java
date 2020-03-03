/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

//all required libraries for the robot

package frc.robot;
import edu.wpi.first.cameraserver.CameraServer;
//pneumatics library. Not included
// import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private Joystick stick = new Joystick(0);
  XboxController xbox = new XboxController(0);

  // reprogram every motor excluding the drivebase motors
  VictorSP leftDrive, rightDrive, LeftHang1, leftHang2, rightHang1, rightHang2, motorLift/* , rightLift*/;
  PWMVictorSPX intake;
  DifferentialDrive robotDrive;
  SpeedControllerGroup leftB, rightB;
  //No pnematics will be used on 2020 robot
  // DoubleSolenoid soleRight, soleLeft;
  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    leftDrive = new VictorSP(7);
    rightDrive = new VictorSP(6);
    SpeedControllerGroup leftDB = new SpeedControllerGroup(leftDrive);
    SpeedControllerGroup rightDB = new SpeedControllerGroup(rightDrive);
    //setting drive base type
    robotDrive = new DifferentialDrive(leftDB, rightDB);
    // Hang
    LeftHang1 = new VictorSP(4);
    leftHang2 = new VictorSP(3);
    rightHang1 = new VictorSP(0);
    rightHang2 = new VictorSP(1);
    motorLift = new VictorSP(5);
    // rightLift = new VictorSP(2);
    //Intake
    intake = new PWMVictorSPX(8);
    //Camera
    CameraServer.getInstance().startAutomaticCapture(0);
    CameraServer.getInstance().startAutomaticCapture(1);
  }
  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }
  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }
  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }
  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {

  boolean aButton = stick.getRawButton(1);
  boolean bButton = stick.getRawButton(2);
  boolean xButton = stick.getRawButton(3);
  boolean yButton = stick.getRawButton(4);
  boolean RB = stick.getRawButton(6);
  double RT = stick.getRawAxis(3);
  boolean LB = stick.getRawButton(5);
  double LT = stick.getRawAxis(2);
  //Pneumatics controller
  // double joyR = stick.getRawAxis(5);
  // double joyL = stick.getRawAxis(1);
  robotDrive.tankDrive(-stick.getRawAxis(1), -stick.getRawAxis(5), true);
    if (RB) { //gets R upper trigger
    //Lift UP
    motorLift.set(-0.75);
    //rightLift.set(0.75);
    }
    else if (RT > 0) {
    // Lift DOWN
    motorLift.set(0.75);
    //rightLift.set(-0.75);
    }

    else if (LB) {
    // Intake based on trigger position
    intake.set(1);
    }
    else if (LT > 0) {
    // Shoot ball
    intake.set (-LT);
    }

    else if (bButton) {
    //Deploy hang
    LeftHang1.set(0.5);
    leftHang2.set(0.5);
    rightHang1.set (-0.5);
    rightHang2.set (-0.5);
    }
    else if (xButton) {
    //Hang robot
    LeftHang1.set (-0.5);
    leftHang2.set(-0.5);
    rightHang1.set(0.5);
    rightHang2.set(0.5);
    }

    // no Pneumatics will be used for 2020
    
    // else if (aButton) {
    // //clamp onto HAB w/ Pneumatics
    // soleRight.set(DoubleSolenoid.Value.kForward);
    // soleLeft.set(DoubleSolenoid.Value.kForward);
    // }
    // 
    //
    // else if (yButton) {
    // //Unclamp HAB w/ Pneumatics
    // soleRight.set(DoubleSolenoid.Value.kReverse);
    // soleLeft.set(DoubleSolenoid.Value.kReverse);
    // }

    //holds the motors in place
    else {
    LeftHang1.set(0);
    leftHang2.set(0);
    rightHang1.set (0);
    rightHang2.set(0);
    motorLift.set(0);
    // rightLift.set(0);
    intake.set(0);
    }
  }
  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}

/* 
unused buttons
A
Y

*/

/*
14 motors

*/