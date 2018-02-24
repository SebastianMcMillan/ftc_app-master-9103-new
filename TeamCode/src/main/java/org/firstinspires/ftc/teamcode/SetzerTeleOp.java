package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcontroller.external.samples.SensorREVColorDistance;

/**
 * Created by Sebastian McMillan on 2/6/18.
 */

@TeleOp(name= "Setzer TeleOp", group = "Linear OpMode")

public class SetzerTeleOp extends LinearOpMode {
    //Initializing The Robot

    private ElapsedTime runtime = new ElapsedTime();

    //Declare Motors
    private DcMotor backLeft; //Port 0 Hub 1
    private DcMotor backRight; //Port 1 Hub 1
    private DcMotor frontLeft; //Port 2 Hub 1
    private DcMotor frontRight; //Port 3 Hub 1

    private DcMotor glyphMotor; //Port 0 Hub 2

    //Declare Servos
    private Servo glyphLeft; //Port 0 Hub 1
    private Servo glyphRight; //Port 1 Hub 1

    private Servo jewelServo; //Port 2 Hub 1

    private Servo topGlyphLeft;
    private Servo topGlyphRight;


    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        //HW Map DC Motors
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        glyphMotor = hardwareMap.dcMotor.get("glyphMotor");

        //HW Map Servos
        glyphLeft = hardwareMap.servo.get("glyphLeft");
        glyphRight = hardwareMap.servo.get("glyphRight");

        jewelServo = hardwareMap.servo.get("jewelServo");

        topGlyphLeft = hardwareMap.servo.get("topGlyphLeft");
        topGlyphRight = hardwareMap.servo.get("topGlyphRight");


        waitForStart();
        runtime.reset();

        glyphLeft.setPosition(0); // 0.7 is the open position for glyph servo Left
        glyphRight.setPosition(1); // 0.3 is the open position for glyph servo Right
        topGlyphRight.setPosition(0);
        topGlyphLeft.setPosition(1);

        while (opModeIsActive()) {

            int positionGlyphMotor = glyphMotor.getCurrentPosition();


            telemetry.addData("Encoder glyphMotor Position", positionGlyphMotor);
            telemetry.update();

            jewelServo.setPosition(1);

            if (gamepad1.left_stick_x != 0 || gamepad1.left_stick_y != 0) /*If the left stick is not neutral...*/ {
                double drive = -gamepad1.left_stick_y; //Set the Drive to the negative value of the y-axis value
                double turn = gamepad1.left_stick_x; //Set the turn to the value of the x-axis value

                double leftPower;
                double rightPower;
                leftPower = Range.clip(drive + turn, -1.0, 1.0); //fun math
                rightPower = Range.clip(drive - turn, -1.0, 1.0); //fun math 2

                frontLeft.setPower(leftPower * 1.5); //These are hopefully self-explanatory.
                frontRight.setPower(rightPower * 1.5);
                backLeft.setPower(leftPower * 1.5);
                backRight.setPower(rightPower * 1.5);
            }
            if (gamepad1.left_stick_x == 0 || gamepad1.left_stick_y == 0){

                frontLeft.setPower(0); //These are hopefully self-explanatory.
                frontRight.setPower(0);
                backLeft.setPower(0);
                backRight.setPower(0);
            }
            if (gamepad1.a){

                glyphLeft.setPosition(0.25);
                glyphRight.setPosition(0.69);
                topGlyphRight.setPosition(0.25);
                topGlyphLeft.setPosition(0.72);
            }
            if (gamepad1.b){

                glyphLeft.setPosition(0.15);
                glyphRight.setPosition(0.79);
                topGlyphRight.setPosition(0.15);
                topGlyphLeft.setPosition(0.82);
            }
            if (gamepad1.dpad_up && positionGlyphMotor >= 5000 ){

                glyphMotor.setPower(0);
            }

            if (gamepad1.dpad_down && positionGlyphMotor <= 0 ){

                glyphMotor.setPower(0);
            }

            if (gamepad1.dpad_up && positionGlyphMotor < 5000){

                glyphMotor.setPower(1);
            }

            if (gamepad1.dpad_down && positionGlyphMotor > 0){

                glyphMotor.setPower(-1);
            }

            if (!gamepad1.dpad_down && !gamepad1.dpad_up) {

                glyphMotor.setPower(0);
            }

            if (gamepad1.x) {

                jewelServo.setPosition(0);
            }

            if (gamepad1.y) {

                jewelServo.setPosition(1);
            }


        }
    }
}
