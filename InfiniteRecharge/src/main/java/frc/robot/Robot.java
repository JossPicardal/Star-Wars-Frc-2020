/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

//all required libraries for the robot

package frc.robot;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
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
  // declares the type of speed controller the moter is using
  VictorSP leftDrive, rightDrive, cascadeLift, controlPanel, leftFlywheel, rightFlywheel, intake;
  PWMVictorSPX leftHang, rightHang;
  DifferentialDrive robotDrive;
  SpeedControllerGroup leftB, rightB;
  
  // This function is run when the robot is first started up and should be
  // used for any initialization code.
  
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    leftDrive = new VictorSP(0);     // y cabled 2 motors
    rightDrive = new VictorSP(1);    // y cabled 2 motors
    SpeedControllerGroup leftDB = new SpeedControllerGroup(leftDrive);
    SpeedControllerGroup rightDB = new SpeedControllerGroup(rightDrive);
    //setting drive base type
    robotDrive = new DifferentialDrive(leftDB, rightDB);
    // Hang
    intake = new VictorSP(2);       // y cabled 2 motors
    leftFlywheel = new VictorSP(3);
    rightFlywheel = new VictorSP(4);
    controlPanel = new VictorSP(5);
    leftHang = new PWMVictorSPX(6);      // y cabled 2 motors
    rightHang = new PWMVictorSPX(7);     // y cabled 2 motors
    cascadeLift = new VictorSP(8);
     
    // rightLift = new VictorSP(2);
    //Intake
    //some problem might arise with using VictorSPX with VictorSP
    // intake = new PWMVictorSPX(8);
    //Starts camera(s)
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

  // This part of the program gets the current value of the buttons on the controller
  public void teleopPeriodic() {
    
    boolean aButton = stick.getRawButton(1);
    boolean bButton = stick.getRawButton(2);
    boolean xButton = stick.getRawButton(3);
    boolean yButton = stick.getRawButton(4);
    boolean RB = stick.getRawButton(6);
    double RT = stick.getRawAxis(3);
    boolean LB = stick.getRawButton(5);
    double LT = stick.getRawAxis(2);

    // unused buttons 
    // boolean back = stick.getRawButton(7);
    // boolean start = stick.getRawButton(8);
    // boolean leftStickButton = stick.getRawButton(9);
    // boolean rightStickButton = stick.getRawButton(10);
    robotDrive.tankDrive(-stick.getRawAxis(1), -stick.getRawAxis(5), true);
    
/*
Left Drivebase        | Left stick
Right Drivebase       | Right Stick
Intake                | Right Bumper Intake, Right Trigger Outtakes
Left & Right Flywheel | Left Bumper Intake, Left Trigger Outtakes
Control Panel         | Y button goes CW, X button goes CCW
Rope Hang             | Down Dpad pulls rope in, Up Dpad Pushes rope
Cascade Lift          | Up Dpad extends lift, right Dpad brings down lift

aButton to extend both the rope and lift, and two other buttons to 
retract both at different times.
**NOTE** Cascade lift and rope should go up at the the same time but go down seperately
*/

    if (LB) { // Left bumper is pressed
      // Shoots ball
      leftFlywheel.set(1);
      rightFlywheel.set(1);
    }
    else if (LT > 0) { // Left trigger is pressed
      // Pulls ball back in
      leftFlywheel.set(-1);
      rightFlywheel.set(-1);
    }

    else if (RB) {
    // Intake ball
      intake.set(1);
    }
    else if (RT > 0) {
    // Push ball out
    // intake.set (-RT);
    }

    else if (aButton) {
      //comment function here
    }
    else if (bButton) {
      //comment function here
    }
    
    
    else if (xButton) {
      // motor turns counterclockwise
      controlPanel.set(-0.5);
    }
    
    else if (yButton) {
      // motor turns clockwise
      controlPanel.set(0.5);
    }

    //holds the motors in place
    else {
    leftHang.set(0);
    leftHang.set(0);
    rightHang.set (0);
    cascadeLift.set(0);
    //anything added after this comment
    //was added just in case I needed to 
    //add new motors.
    controlPanel.set(0);
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
more notes...
Variable           |  Port   |Motor Count| Victor # |
Left drivebase     | 1 port  | 2 motors  | Victor 0 | Left Stick
right drivebase    | 1 port  | 2 motors  | Victor 1 | Right Stick
Intake             | 1 port  | 2 motors  | Victor 2 | 
Left Flywheel      | 1 port  | 1 motor   | Victor 3 |
Right Flywheel     | 1 port  | 1 motor   | Victor 4 |
Control Panel      | 1 port  | 1 motor   | Victor 5 |
Left hang          | 1 port  | 2 motors  | Victor 6 |
Right Hang         | 1 port  | 2 motors  | Victor 7 |
Cascade lift       | 1 port  | 1 motor   | Victor 8 |
*/

/*
`Left Drivebase        | Left stick
`Right Drivebase       | Right Stick
`Intake                | Right Bumper Intake, Right Trigger Outtakes
`Left & Right Flywheel | Left Bumper fly's ball, Left Trigger reverses ball in
`Control Panel         | Y button goes CW, X button goes CCW
Rope Hang             | Down Dpad pulls rope in, Up Dpad Pushes rope
Cascade Lift          | Up Dpad extends lift, right Dpad brings down lift

a button to extend both the rope and lift, and two other buttons to 
retract both at different times.
**NOTE** Cascade lift and rope should go up at the
         the same time but go down seperately
*/