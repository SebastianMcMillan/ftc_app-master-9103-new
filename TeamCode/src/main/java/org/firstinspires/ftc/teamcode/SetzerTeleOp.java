package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
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

    private DcMotor relicMotor1; //Port 1 Hub 2
    private Servo relicServo2; //Port 2 Hub 2
    private Servo relicServo3; //Port 3 Hub 2

    private DcMotor glyphMotor; //Port 0 Hub 2

    //Declare Servos
    private Servo glyphLeft; //Port 0 Hub 1
    private Servo glyphRight; //Port 1 Hub 1

    private Servo jewelServo; //Port 2 Hub 1


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

        relicMotor1 = hardwareMap.dcMotor.get("relicMotor1");

        //HW Map Servos
        glyphLeft = hardwareMap.servo.get("glyphLeft");
        glyphRight = hardwareMap.servo.get("glyphRight");

        relicServo2 = hardwareMap.servo.get("relicServo2");
        relicServo3 = hardwareMap.servo.get("relicMotor3");

        jewelServo = hardwareMap.servo.get("jewelServo");


        waitForStart();
        runtime.reset();

        glyphLeft.setPosition(0.7); // 0.7 is the open position for glyph servo Left
        glyphRight.setPosition(0.3); // 0.3 is the open position for glyph servo Right

        while (opModeIsActive()) {

            int positionGlyphMotor = glyphMotor.getCurrentPosition();
            int positionRelicMotor1 = relicMotor1.getCurrentPosition();

            telemetry.addData("Encoder glyphMotor Position", positionGlyphMotor);
            telemetry.addData("Encoder relicMotor1", positionRelicMotor1);
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

                glyphLeft.setPosition(0.9);
                glyphRight.setPosition(0.1);
            }
            if (gamepad1.b){

                glyphLeft.setPosition(0.7);
                glyphRight.setPosition(0.3);
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

            if (gamepad2.right_bumper){

                relicServo3.setPosition(0.3);
            }

            if (gamepad2.left_bumper){

                relicServo3.setPosition(-0.3);
            }

            if (!gamepad2.right_bumper && !gamepad2.left_bumper) {

                relicServo3.setPosition(0);
            }

            if (gamepad2.right_trigger != 0) {

                relicMotor1.setPower(gamepad2.right_trigger / 1.5);
            }

            if (gamepad2.left_trigger !=0) {

                relicMotor1.setPower(-gamepad2.left_trigger / 1.5);
            }

            if (gamepad2.right_trigger == 0 && gamepad2.left_trigger == 0) {

                relicMotor1.setPower(0);
            }

            if (gamepad2.right_stick_y != 0) {

                relicServo2.setPosition(gamepad2.right_stick_y);
            }


            if (gamepad2.right_stick_y == 0) {

                relicServo2.setPosition(0.5);
            }






        }
    }
}
