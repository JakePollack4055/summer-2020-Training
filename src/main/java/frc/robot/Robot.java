/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2020 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
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

  private CANSparkMAX driveLeftSpark1;
  private CANSparkMAX driveLeftSpark2;
  private CANSparkMAX driveLeftSpark3;
  private CANSparkMAX driveLeftSpark4;
  private CANSparkMAX driveRightSpark1;
  private CANSparkMAX driveRightSpark2;
  private CANSparkMAX driverightSpark3;
  private CANSparkMAX driveRightSpark4;
  private WPI_TalonSRX cargoRollersTalon;
  private WPI_TalonSRX hatchRollersTalon;
  private XboxController xbox1;
  private XboxController xbox2;
  private DifferentialDrive driveTrain;
  private DoubleSolenoid slideSolenoid;
  private DoubleSolenoid liftSolenoid;
  private int solenoidToggle =0;
  private Timer timer;
  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    cargoRollersTalon = new WPI_TalonSRX(8);
    hatchRollersTalon = new WPI_TalonSRX(10);
    xbox1 =new XboxController();
    xbox2 =new XboxController();
    driveLeftSpark1 = new CANSparkMAX(12, MotorType.kBrushless);
    driveLeftSpark2 = new CANSparkMAX(13, MotorType.kBrushless);
    driveLeftSpark3 = new CANSparkMAX(14, MotorType.kBrushless);
    driveLeftSpark4 = new CANSparkMAX(15, MotorType.kBrushless);
    driveRightSpark1 = new CANSparkMAX(4, MotorType.kBrushless);
    driveRightSpark2 = new CANSparkMAX(5, MotorType.kBrushless);
    driveRightSpark3 = new CANSparkMAX(6, MotorType.kBrushless);
    driveRightSpark4 = new CANSparkMAX(7, MotorType.kBrushless);
    rightSpeedControllerGroup = new SpeedControllerGroup(driveRightSpark1, driveRightSpark2, driveRightSpark3, driveRightSpark4);
    leftSpeedControllerGroup = new SpeedControllerGroup(driveLeftSpark1, driveLeftSpark2, driveLeftSpark3, driveLeftSpark4);
    driveTrain = new DifferentialDrive(leftSpeedControllerGroup, rightSpeedControllerGroup);
    slideSolenoid = new DoubleSolenoid(1,5);
    liftSolenoid = new DoubleSolenoid(0,4);
    timer = new Timer();
    slideSolenoid.set(kReverse);
    liftSolenoid.set(kReverse);

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
    timer.start;
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        if(timer.get() < 5){
          leftSpeedControllerGroup.set(1);
          rightSpeedControllerGroup.set(1);
        }
        if(timer.get() > 5 && timer.get() < 6){
          cargoRollersTalon.set(1);
        }
        cargoRollersTalon.set(0);
        if(timer.get() > 6 && timer.get() < 9.5){
          leftSpeedControllerGroup.set(-1);
          rightSpeedControllerGroup.set(-1);
        }
        if(timer.get() >9.5 && timer.get() < 10.5){
          cargoRollersTalon.set(-1);
        }
        cargoRollersTalon.set(0);
        break;
      case kDefaultAuto:
      default:
        if(timer.get() < 2){
          leftSpeedControllerGroup.set(-1);
          rightSpeedControllerGroup.set(1);
        }
        liftSolenoid.set(kForward);
        hatchSolenoid.set(kForward);
        if(timer.get() > 3 && timer.get() < 4){
          hatchRollersTalon.set(1);
        }
        hatchRollersTalon.set(0);
        if(timer.get() > 4 && timer.get() < 5.5){
          leftSpeedControllerGroup.set(-1);
          rightSpeedControllerGroup.set(1);
        }
        if(timer.get()>5.5 && timer.get()<6.5){
          hatchRollersTalon.set(-1);
        }
        hatchRollersTalon.set(0);
        hatchSolenoid.set(kReverse);
        liftSolenoid.set(kReverse);
        break;
    }
  }

  /**
   * This function is called once when teleop is enabled.
   */
  @Override
  public void teleopInit() {
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    

    cargoRollersTalon.set(xbox1.getx());
    leftSpeedControllerGroup.set(Math.abs(xbox1.getTriggerAxis(kRight)-xbox1.getTriggerAxis(kLeft) + xbox.getx(kLeft));
    rightSpeedControllerGroup.set(Math.abs(xbox1.getTriggerAxis(kRight)-xbox1.getTriggerAxis(kLeft) - xbox.getx(kLeft));
    
    if(xbox1.getXButton){
      if(xbox1.getx(kleft>0.0){
        leftSpeedControllerGroup.set(1.0);
        rightSpeedControllerGroup.set(-1.0);
      }
      if(xbox1.getx(kleft<0.0){
        leftSpeedControllerGroup.set(-1.0);
        rightSpeedControllerGroup.set(1.0);
      }
    }
    if(xbox2.getStartButton && solenoidToggle ==0){
      liftSolenoid.set(kForward);
      hatchSolenoid.set(kForward);
      solenoidToggle=1;
    }
    if(xbox2.getStartButton && solenoidToggle ==1){
      liftSolenoid.set(kReverse);
      hatchSolenoid.set(kReverse;
      solenoidToggle =0;
    }
    if(xbox2.getBumper(kRight){
      hatchRollersTalon.set(1);
    }
    if(xbox2.getBumper(kleft){
      cargoRollersTalon.set(1);
    }
    if(xbox2.getTrigger(kRight){
      hatchRollersTalon.set(-1);
    }
    if(xbox2.getTrigger(kLeft){
      hatchRollersTalon.set(-1);
    }

    
    }

    }
  }

  /**
   * This function is called once when the robot is disabled.
   */
  @Override
  public void disabledInit() {
  }

  /**
   * This function is called periodically when disabled.
   */
  @Override
  public void disabledPeriodic() {
  }

  /**
   * This function is called once when test mode is enabled.
   */
  @Override
  public void testInit() {
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
