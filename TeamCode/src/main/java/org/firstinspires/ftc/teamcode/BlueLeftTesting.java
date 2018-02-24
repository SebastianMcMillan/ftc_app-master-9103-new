package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.Locale;

@Autonomous(name = "BlueLeftTesting", group = "Sensor")

public class BlueLeftTesting extends LinearOpMode {

    BNO055IMU imu;
    Orientation angles;
    Acceleration gravity;

    ColorSensor sensorColor;

    private DcMotor backLeft; //Port 0 Hub 1
    private DcMotor backRight; //Port 1 Hub 1
    private DcMotor frontLeft; //Port 2 Hub 1
    private DcMotor frontRight; //Port 3 Hub 1

    private DcMotor glyphMotor; //Port 0 Hub 2

    //Declare Servos
    private Servo glyphLeft; //Port 0 Hub 1
    private Servo glyphRight; //Port 1 Hub 1

    private Servo jewelServo; //Port 2 Hub 1

    VuforiaLocalizer vuforia;
    private Servo topGlyphRight;
    private Servo topGlyphLeft;




    @Override

    public void runOpMode() throws InterruptedException {

        //HW Map Servos
        glyphLeft = hardwareMap.servo.get("glyphLeft");
        glyphRight = hardwareMap.servo.get("glyphRight");

        jewelServo = hardwareMap.servo.get("jewelServo");

        topGlyphLeft = hardwareMap.servo.get("topGlyphLeft");
        topGlyphRight = hardwareMap.servo.get("topGlyphRight");

        telemetry.update();




        waitForStart();

        while (opModeIsActive()) {
            if (getRuntime() > 0 && getRuntime() <= 5) {
                glyphLeft.setPosition(0.25);
                glyphRight.setPosition(0.7);
                topGlyphRight.setPosition(0.25);
                topGlyphLeft.setPosition(0.7);
                //jewelServo.setPosition(1);
            }
        }

            if (getRuntime() > 5 && getRuntime() <= 7) { //CHANGE VALUES AFTER FIRST TEST
                glyphLeft.setPosition(0.25);
                glyphRight.setPosition(0.7);
                topGlyphRight.setPosition(0.25);
                topGlyphLeft.setPosition(0.7);
                //jewelServo.setPosition(1);
            }

    }

    }