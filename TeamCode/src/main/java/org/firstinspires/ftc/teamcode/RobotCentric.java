package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.hardware.bosch.BNO055IMU;
import org.firstinspires.ftc.robotcore.external.android.AndroidSoundPool;
import android.media.MediaPlayer;

@TeleOp

public class RobotCentric extends LinearOpMode {
     Gamepad driveGamepad = new Gamepad();
     Gamepad manipulatorGamepad = new Gamepad();

     // Variables
     public int armPose = 0;
     public double wrist = 4;
    @Override
    
    //Defines the motor
    public void runOpMode() {
        // Drive Motor
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontLeft"); //Port 0
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight"); //Port 1
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "backLeft"); 
        DcMotor backRight = hardwareMap.get(DcMotor.class, "backRight");

        // Linear Slide
        //DcMotor leftSlide = hardwareMap.get(DcMotor.class, "leftSlide"); //Slot 0
        
       DcMotor armOne = hardwareMap.get(DcMotor.class,"armOne");
       Servo clawRotate = hardwareMap.get(Servo.class,"clawRotate");
       Servo clawClamp = hardwareMap.get(Servo.class,"clawClamp");

       // Setting arm position
       armOne.setTargetPosition(0);
       armOne.setMode(DcMotor.RunMode.RUN_TO_POSITION);
       armOne.setPower(.65);

       // Setting up the IMU
        /* It doesn't work :(

        BNO055IMU.Parameters parameters = new BNO055IMU imu.Parameters(
                new RevHubOrientationOnRobot (
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.RIGHT
                )
        );
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        imu.initialize(parameters);*/
        BNO055IMU imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu.initialize(parameters);


        //AndroidSoundPool horns = new AndroidSoundPool();
        MediaPlayer mediaPlayer;
        mediaPlayer = MediaPlayer.create(hardwareMap.appContext, R.raw.horn);


        //horns.initialize(1);


        waitForStart();
        
        if (isStopRequested()) {
            mediaPlayer.release();
            return;
        }
        
        while (opModeIsActive()) {
            // Getting inputs
            driveGamepad.copy(gamepad1);
            manipulatorGamepad.copy(gamepad2);

            //
            // Drive
            //
            float y = -driveGamepad.left_stick_y;
            float x = driveGamepad.left_stick_x;
         
             if (driveGamepad.dpad_left) {
                 x = -0.25f;
             }
             if (driveGamepad.dpad_right) {
                 x = 0.25f;
             }
             if (driveGamepad.dpad_up) {
                 y = 0.25f;
             }
             if (driveGamepad.dpad_down) {
                 y = -0.25f;
             }

             //
            // Linear Slide
            //
             //For up-movement of linear slide
             boolean verticalUp = manipulatorGamepad.dpad_up;
             //For down-movement of linear slide
             boolean verticalDown = manipulatorGamepad.dpad_down;
             double rotateRight = manipulatorGamepad.right_trigger;
             double rotateLeft = manipulatorGamepad.left_trigger;
             double clawPosX = manipulatorGamepad.left_stick_x;
             double clawPosY = -manipulatorGamepad.left_stick_y;

             boolean clawOpen = manipulatorGamepad.left_bumper;
             boolean clawClose = manipulatorGamepad.right_bumper;
             double wheelCPR = 423.2116; //Counts per revolution
             double linearCPR = 72.1;


             /*double currentHeading = imu.getAngularOrientation().firstAngle;
             double headingOff = currentHeading;
             double degreeOff = currentHeading-headingOff;

             double xRot = Math.cos(degreeOff);
             double yRot = Math.sin(degreeOff);*/

            double combinedRotation = .85*(rotateRight-rotateLeft);
            double fl = (y+x+combinedRotation);
            double fr = (x-y-combinedRotation);
            double bl = (y-x+combinedRotation);
            double br = (-y-x-combinedRotation);

            //stops it from going greater than 1/-1
            double maxNumber = Math.max(Math.abs(x)+Math.abs(y)+Math.abs(combinedRotation),1);
             //powers the motor for wheels
            frontLeft.setPower(fl/maxNumber*0.5);
            frontRight.setPower(fr/maxNumber*0.5);
            backLeft.setPower(bl/maxNumber*0.5);
            backRight.setPower(br/maxNumber*0.5);

            if (driveGamepad.start) {
                if (!mediaPlayer.isPlaying())
                    mediaPlayer.start();
                else {
                    mediaPlayer.pause();
                    mediaPlayer.seekTo(0);
                }
            }
        
        //temp code
        
        /*
        if (verticalUp) {
            leftSlide.setPower(0.5);
        }                       //Prob works
        if (verticalDown) {
            leftSlide.setPower(-0.5);
        }
            if (verticalUp && verticalDown == false) {
            leftSlide.setPower(0);
        }
        */
       
            
        if (manipulatorGamepad.dpad_up) {
            armPose -= 5;
        }
        if (manipulatorGamepad.dpad_down) {
            armPose += 5;
        }
        if (manipulatorGamepad.dpad_right) {
            armPose--;
        }
        if (manipulatorGamepad.dpad_left) {
            armPose++;
        }
        if (armPose <= -126) {
            armPose = -126;
        }
        if (armPose >= -5) {
            armPose = -5;
        }
        
        
        if (manipulatorGamepad.cross) {
            armPose = -7;
        }
        if (manipulatorGamepad.triangle) {
            armPose = -126;
        }
        if (manipulatorGamepad.square) {
            armPose = -72;
        }

        // if (Math.abs(armOne.getCurrentPosition() - armThing) <= 2) {
        //     armOne.setTargetPosition(armOne.getCurrentPosition());
        //     armTwo.setTargetPosition(armTwo.getCurrentPosition());
        // } else {
        //     armOne.setTargetPosition(armThing);
        //     armTwo.setTargetPosition(armThing);
        // }
        
        armOne.setTargetPosition(armPose);
            
        telemetry.addData("Arm One: ", armOne.getCurrentPosition());

       
       if (clawOpen) {
           clawClamp.setPosition(1);
       }
       if (clawClose) {
           clawClamp.setPosition(0);
       }
       
       if (manipulatorGamepad.left_trigger >= .9) {
           wrist -= .05;
       }
       if (manipulatorGamepad.right_trigger >= .9) {
           wrist += .05;
       }
       if (wrist >= 1) {
           wrist = 1;
       }
       if (wrist <= 0) {
           wrist = 0;
       }
       if (manipulatorGamepad.b) {
           wrist = .4;
       }
       clawRotate.setPosition(wrist);
       
    //     double clawPosition = clawClamp.getPosition();
    //     if (clawPosY > 0) {
    //         clawRotate.setPosition(0.5);
    //     } 
    //     if (clawPosY < 0) {
    //         clawRotate.setPosition(0);
    //     }
    //     if (clawPosX < 0) {
    //         clawRotate.setPosition(0.25);
    //     }
    //     if (clawPosX > 0) {
    //         clawRotate.setPosition(0.75);
    //     }
    // double clawMove = 0;
    //     //more if statements YAY!!!
    //     int clawTest = 0;
    //     if (clawOpen) {
    //         clawClamp.setPosition(0);
    //         clawTest = 1;
    //         telemetry.addData("Actually Reads Input: ", clawTest);

    //     }
    //     if (clawClose) {
    //         clawClamp.setPosition(1);
    //         clawTest = 2;
    //         telemetry.addData("Reads Input: ", clawTest);
    //     }

         
  
        /*double botHeading = imu.getAngularOrientation().firstAngle;
         
         double triX = x * Math.cos(botHeading) - y * Math.sin(botHeading);
         double triY = x * Math.sin(botHeading) + y * Math.cos(botHeading);
         telemetry.addData("TriX: ",triX);
         telemetry.addData("TriY: ",triY);
         telemetry.addData("botHeading: ",botHeading);*/
         //fl is front left, br is back right, etc.
        
         /*
         double fl = (triY+triX);
         double fr = (triX-triY);
         double bl = (triY-triX);
         double br = (-triY-triX);
         */
        //stops it from going greater than 1/-1
        //  maxNumber = Math.max(Math.abs(x)+Math.abs(y),1);
        //  //powers the motor for wheels
        // frontLeft.setPower(fl/maxNumber);
        // frontRight.setPower(fr/maxNumber);
        // backLeft.setPower(bl/maxNumber);
        // backRight.setPower(br/maxNumber); 
        telemetry.update();
    
        
       /*int soundID = hardwareMap.apContext.getResources().getIdentifier("horn", "raw", hardwareMap.appContext.getPackageName());
         if (soundID != 0 && honk == True) {
             SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, soundID);
         }*/

        
        
        
        
        
        
        }
    }
}



