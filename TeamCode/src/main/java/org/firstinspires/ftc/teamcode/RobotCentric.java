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
     public float armPose = 0;
     public double wrist = 4;
     public float slidePose = 0;
    @Override
    
    //Defines the motor
    public void runOpMode() {
        // Drive Motor
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontLeft"); //Port 0
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight"); //Port 1
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "backLeft"); 
        DcMotor backRight = hardwareMap.get(DcMotor.class, "backRight");
        DcMotor leftSlide = hardwareMap.get(DcMotor.class, "leftSlide");
        DcMotor rightSlide = hardwareMap.get(DcMotor.class,"rightSlide");
        
       DcMotor armOne = hardwareMap.get(DcMotor.class,"armOne");
       Servo clawRotate = hardwareMap.get(Servo.class,"clawRotate");
       Servo clawClamp = hardwareMap.get(Servo.class,"clawClamp");

       // Setting arm position

       leftSlide.setTargetPosition(0);
       leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);



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
            double rotateRight = driveGamepad.right_trigger;
            double rotateLeft = driveGamepad.left_trigger;

             //
            // Linear Slide
            //
             //For up-movement of linear slide
            double rx = rotateRight - rotateLeft;
             boolean clawOpen = manipulatorGamepad.left_bumper;
             boolean clawClose = manipulatorGamepad.right_bumper;




            double fl = (y+x+rx);
            double fr = (x-y+rx);
            double bl = (y-x+rx);
            double br = (-y-x+rx);

            //stops it from going greater than 1/-1
            double maxNumber = Math.max(Math.abs(x)+Math.abs(y)+Math.abs(rx),1);
             //powers the motor for wheels
            frontLeft.setPower(fl/maxNumber*0.5);
            frontRight.setPower(fr/maxNumber*0.5);
            backLeft.setPower(bl/maxNumber*0.5);
            backRight.setPower(br/maxNumber*0.5);

            if (driveGamepad.guide) {
                if (!mediaPlayer.isPlaying())
                    mediaPlayer.start();
                else {
                    mediaPlayer.pause();
                    mediaPlayer.seekTo(0);
                }
            }
        
        //temp code
        


            // Arm Movement
            if (manipulatorGamepad.dpad_up) {
                slidePose = 1;
            }
            if (manipulatorGamepad.dpad_down) {
                slidePose = -1;
            }
            if (manipulatorGamepad.dpad_right) {
                slidePose = -.5f;
            }
            if (manipulatorGamepad.dpad_left) {
                slidePose = .5f;
            }
            if (slidePose < -1) {
                slidePose = -1;
            }
            if (slidePose > 1) {
                slidePose = 1;
            }
            //Slide Pose
            if (manipulatorGamepad.cross) {
                armPose = 1;
            }
            if (manipulatorGamepad.triangle) {
                armPose = -1;
            }
            if (manipulatorGamepad.square) {
                armPose = 0.5f;
            }
            if (manipulatorGamepad.circle) {
                armPose = -0.5f;
            }
            if (armPose < -1) {
                armPose = -1;
            }
            if (armPose > 1) {
                armPose = 1;
            }
            if (manipulatorGamepad.share) {
                armPose = 0;
            }



            armOne.setPower(armPose);
            leftSlide.setPower(slidePose);
            
        telemetry.addData("Arm One: ", armOne.getCurrentPosition());
        telemetry.addData("Left Slide: ", leftSlide.getCurrentPosition());


       
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
            if (manipulatorGamepad.right_stick_button) {
                wrist = .4;
            }


       clawRotate.setPosition(wrist);
       

        telemetry.update();
    
        

        
        
        
        
        
        
        }
    }
}
