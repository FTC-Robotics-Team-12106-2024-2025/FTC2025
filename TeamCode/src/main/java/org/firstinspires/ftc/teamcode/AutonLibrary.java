package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

// Strafe 20 for half a tile, 40 for a tile

public class AutonLibrary extends LinearOpMode {
    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor backLeft;
    public DcMotor backRight;
    public DcMotor slide;
    public DcMotor armMotor;
    public CRServo intakeOne;
    public CRServo intakeTwo;
    public ColorSensor color;
    public DistanceSensor colorDistance;
    public ElapsedTime timer;
    public Servo wristServo;


    public void runOpMode() {
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        slide = hardwareMap.get(DcMotor.class, "slide");
        armMotor = hardwareMap.get(DcMotor.class,"armMotor");
        intakeOne = hardwareMap.get(CRServo.class,"intakeOne");
        int[][] thresholds = {{1,1,1},{1,1,1},{1,1,1}};// ind 0 = red, ind 1 = blue, ind 2 = green; List of thresholds per sample; red, blue, yellow
        String[] colorData = {"Filtering for RED samples","Filtering for BLUE samples","Filtering for YELLOW samples"};
        int[][] sampleHave = {{211,41,37},{55,89,223},{241,178,14}};
        int[][] sampleLook = {{233,141,138},{155,172,239},{246, 217, 135}};
        waitForStart();

        autonCommands();
    }

    public void autonCommands() {

    }

    public final void moveWheel(double x,double y) {
         double fl = (y+x);
         double fr = (x-y);
         double bl = (y-x);
         double br = (-y-x);
        //stops it from going greater than 1/-1
         double maxNumber = Math.max(Math.abs(x)+Math.abs(y),1);
         //powers the motor for wheels
        frontLeft.setPower(fl/maxNumber);
        frontRight.setPower(fr/maxNumber);
        backLeft.setPower(bl/maxNumber);
        backRight.setPower(br/maxNumber); 
        //Should do telemetery dataz thingy I hate this.

    }
   public final void turn(boolean rotateLeft, boolean rotateRight) {
        double fl, fr, bl, br;

        fl = fr = bl = br = 0;
        if (rotateRight) {
            fl = 0.5;
            fr = 0.5;
            bl = 0.5;
            br = 0.5;
         }
         if (rotateLeft) {
             fl = -0.5;
             fr = -0.5;
             bl = -0.5;
             br = -0.5;
         }
         frontLeft.setPower(fl);
         frontRight.setPower(fr);
         backLeft.setPower(bl);
         backRight.setPower(br);
   }

   public final void linearVertical(int verticalPos) {
        slide.setTargetPosition(0);
        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slide.setPower(0.65);
        slide.setTargetPosition(verticalPos);
    }
//I use one function for the entire intake code. Probably will change with color sensor
public final void Intake(double intakeDir) {
        intakeOne.setPower(intakeDir);
        intakeTwo.setPower(intakeDir);
}
//sets the color of the sensor

public final void startSensor(boolean sortActive, int colorFilter) {
        if (sortActive) {
            int[][] thresholds = {{1,1,1},{1,1,1},{1,1,1}};// ind 0 = red, ind 1 = blue, ind 2 = green; List of thresholds per sample; red, blue, yellow
            intakeOne.setPower(-0.75);
            intakeTwo.setPower(-0.75); // powers might be wrong
            int red = thresholds[colorFilter][0];
            int blue = thresholds[colorFilter][1];
            int green = thresholds[colorFilter][2];
            if (colorDistance.getDistance(DistanceUnit.CM) < 2.0) {
                intakeOne.setPower(0);
                intakeTwo.setPower(0);           // tolerance of 10 (can be changed)
                if (Math.abs(color.red() - red) < 10 && Math.abs(color.blue() - blue) < 10 && Math.abs(color.green() - green) < 10) {
                    sortActive = false;
                } else {
                    timer.reset();// might reset too much
                    intakeOne.setPower(0.75);
                    intakeTwo.setPower(0.75);
                    if (timer.milliseconds() >= 500) { // might not resolve if it checks it immediately (checks right after timer is on and never flips direction)
                        intakeOne.setPower(-0.75);
                        intakeTwo.setPower(-0.75);
                    }
                }
            }
        }

}



        //
        //Movement Code
        //

    public final void moveForward (int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100); //Im using tenths of a second
        while (System. currentTimeMillis() < stop) {
            //not even sure if this works or not. We can test it anyway
            moveWheel(0,0.5);
        }
    }
    public final void moveBackward(int tenths) {
            //sets current time
            long durationSucks = System.currentTimeMillis();
            //stops current time
            long stop = (durationSucks + tenths*100);
            while (System. currentTimeMillis() < stop) {
                //not even sure if this works or not. We can test it anyway
                moveWheel(0,-0.5);
        }
    }
    //

    //Hopefully strafe code works
    public final void strafeRight(int tenths) {
            //sets current time
            long durationSucks = System.currentTimeMillis();
            //stops current time
            long stop = (durationSucks + tenths*100);
            while (System. currentTimeMillis() < stop) {
                //not even sure if this works or not. We can test it anyway
                moveWheel(0.5,0);
        }
    }
    public final void strafeLeft(int tenths) {
            //sets current time
            long durationSucks = System.currentTimeMillis();
            //stops current time
            long stop = (durationSucks + tenths*100);
                while (System. currentTimeMillis() < stop) {
                //not even sure if this works or not. We can test it anyway
                moveWheel(-0.5,0);
        }
    }
       public final void rotateLeft(int tenths) {
            //sets current time
            long durationSucks = System.currentTimeMillis();
            //stops current time
            long stop = (durationSucks + tenths*100);
                while (System. currentTimeMillis() < stop) {
                turn(true,false);
        }
    }
    public final void rotateRight(int tenths) {
            //sets current time
            long durationSucks = System.currentTimeMillis();
            //stops current time
            long stop = (durationSucks + tenths*100);
                while (System. currentTimeMillis() < stop) {
                turn(false,true);
        }
    }
    public final void killSwitch(int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100);
        while (System. currentTimeMillis() < stop) {
            //not even sure if this works or not. We can test it anyway
            moveWheel(0,0);
        }
    }

    //
    //Intake Code ig, pretty self explanatory
    //
    public final void intakeIn(int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100);
        while (System. currentTimeMillis() < stop) {
            //not even sure if this works or not. We can test it anyway
            Intake(-0.75);
        }
    }
    public final void intakeOut(int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100);
        while (System. currentTimeMillis() < stop) {
            //not even sure if this works or not. We can test it anyway
            Intake(0.75);
        }
    }
    public final void intakeStop(int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100);
        while (System. currentTimeMillis() < stop) {
            //not even sure if this works or not. We can test it anyway
            Intake(0);
        }
    }


    //
    //Slide Code
    //

    public void slideZero(int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100);
        while (System. currentTimeMillis() < stop) {
            linearVertical(0);
        }
    }
    public void slideQuarter(int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100);
        while (System. currentTimeMillis() < stop) {
            linearVertical(1525);
        }
    }
    public void slideHalf(int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100);
        while (System. currentTimeMillis() < stop) {
            linearVertical(3050);
        }
    }
    public void slideThreeQuarts(int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100);
        while (System. currentTimeMillis() < stop) {
            linearVertical(4575);
        }
    }
    public void slideMax(int tenths) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100);
        while (System. currentTimeMillis() < stop) {
            linearVertical(6100);
        }
    }

    //
    //Arm Code
    //


    public void armPower(int tenths, int armPos) {
        //sets current time
        long durationSucks = System.currentTimeMillis();
        //stops current time
        long stop = (durationSucks + tenths*100);
        while (System. currentTimeMillis() < stop) {
            armMotor.setTargetPosition(armPos);
        }
    }

}
