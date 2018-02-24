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

@Autonomous(name = "BlueLeft", group = "Sensor")

public class NoGlyphAutonBlueLeft extends LinearOpMode {

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

    public void runOpMode() throws InterruptedException{

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

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        // Set up our telemetry dashboard
        composeTelemetry();

        // get a reference to the color sensor.
        sensorColor = hardwareMap.get(ColorSensor.class, "colorSensor");


        // hsvValues is an array that will hold the hue, saturation, and value information.
        float hsvValues[] = {0F, 0F, 0F};

        // values is a reference to the hsvValues array.
        final float values[] = hsvValues;

        // sometimes it helps to multiply the raw RGB values with a scale factor
        // to amplify/attentuate the measured values.
        final double SCALE_FACTOR = 255;

        // get a reference to the RelativeLayout so we can change the background
        // color of the Robot Controller app to match the hue detected by the RGB sensor.
        int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
        final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters1 = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters1.vuforiaLicenseKey = "AVrtV3L/////AAAAmY05oAXJSExMnfA9isTrlL9Wb5BjKpqdWP7WVu7w5TmtQciN1tKAo7JfLoEq0JJWXVjt2so3vtllp36RFQ3Wge/pC38H1yIQSVOs0W2CQ28XuBlqNAGAvvI8Bz8T3Ju/JbxmvWqt0+nZaEzMINHUVQOG3PgvXqizMCpdoyJVW54KG24h4m/Zq6F0AngRm54R5E/GKrVkzmUi/DuPy0ZKwzCKyBWBUKLaU4dpP+WWmRzOz3+IaRrxNOMCaHhZEKH0f55MEcAerGSODxV3YtLZw0+HzChjiFRFVX7WffU9uxu2w6/2RXdMEFrmdtsUuleOODHL8jw1kORgaMXglBk3/mYhjM4Vzj6yiqqKcJ3nQ8a8";
        parameters1.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters1);
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        int vuforiaPosition = 0;
        int LEFT = 5;
        int RIGHT = 10;
        int CENTER = 15;

        angles = imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double runtime = getruntime()();
        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);
        double runtime2;
        double completed = 0;
        double FRpower = 0;
        double FLpower = 0;
        double BLpower = 0;
        double BRpower = 0;
        boolean red = false;
        boolean blue = false;
        double A_ServoValue = 1;
        double LIMpower = 0;
        double RIMpower = 1;
        double TRIMpower = 0;
        double TLIMpower = 1;
        int targetAngle = 0;


        composeTelemetry();
        telemetry.update();

        glyphLeft.setPosition(0.25);
        glyphRight.setPosition(0.7);
        topGlyphRight.setPosition(0.25);
        topGlyphLeft.setPosition(0.7);
        jewelServo.setPosition(1);

        waitForStart();

        while (opModeIsActive()) {

            Color.RGBToHSV((int) (sensorColor.red() * SCALE_FACTOR),
                    (int) (sensorColor.green() * SCALE_FACTOR),
                    (int) (sensorColor.blue() * SCALE_FACTOR),
                    hsvValues);

            telemetry.addData("Alpha", sensorColor.alpha());
            telemetry.addData("Red  ", sensorColor.red());
            telemetry.addData("Green", sensorColor.green());
            telemetry.addData("Blue ", sensorColor.blue());
            telemetry.addData("Hue", hsvValues[0]);

            angles = imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

            Color.RGBToHSV((int) (this.sensorColor.red() * SCALE_FACTOR),
                    (int) (this.sensorColor.green() * SCALE_FACTOR),
                    (int) (this.sensorColor.blue() * SCALE_FACTOR),
                    hsvValues);

            getRuntime();

            runtimeme2 = getruntime()();

            /*if (completed != 1 ){
                runtimeme = getruntime()();
                completed=1;
            }
            runtimeme2 = getruntime()() - runtimeme;*/

            telemetry.addData("runtimeme2", getruntime()());

            telemetry.update();
// turn left, go forward, turn right, reverse, drow jewel manipulator, color sensor on right side do that stuff then turn right then forward to crypto then right forward stop release
            if (getruntime() <= 3) { // literally does nothing for 3 secs

                FRpower = 0;
                FLpower = 0;
                BLpower = 0;
                BRpower = 0;
                A_ServoValue = 1;
            }

            if (getruntime() > 3 && getruntime() <= 5) { // turns left
                FRpower = .5;
                FLpower = 0;
                BLpower = 0;
                BRpower = .5;
            }

            if (getruntime() > 5 && getruntime() <= 7) { // goes forward
                FRpower = .5;
                FLpower = .5;
                BLpower = .5;
                BRpower = .5;
            }

            if (getruntime() > 7 && getruntime() <= 8) { // turns right
                FRpower = 0;
                FLpower = .5;
                BLpower = .5;
                BRpower = 0;
            }

            if (getruntime() > 8 && getruntime() <= 10) { // reverses
                FRpower = -.5;
                FLpower = -.5;
                BLpower = -.5;
                BRpower = -.5;
            }

            if (getruntime() > 10 && getruntime() <= 15) { // does jewel things
                A_ServoValue = 0.4;
                if (hsvValues[0] < 35 && sensorColor.red() > sensorColor.blue()) {
                    telemetry.addLine("This is red");
                    red = true;


                } else if (hsvValues[0] >= 35 && sensorColor.red() < sensorColor.blue()) {
                    telemetry.addLine("This is blue");
                    blue = true;
                } else {
                    telemetry.addLine("What am I doing with my life?");
                }

                if (red == true) {

                    FRpower = 0.5;
                    FLpower = -0.5;
                    BLpower = 0.5;
                    BRpower = -0.5;
                }

                else if (blue == true) {

                    FRpower = -0.5;
                    FLpower = 0.5;
                    BLpower = -0.5;
                    BRpower = 0.5;
                }

                else {

                    FRpower = 0;
                    FLpower = 0;
                    BLpower = 0;
                    BRpower = 0;
                }
            }

            //KAYLAN STOPPED LOOKING AT THINGS HERE
            if (getruntime() > 14 && getruntime() <= 16 ) {

                A_ServoValue = 1;
                FRpower = -0.5;
                FLpower = -0.5;
                BLpower = -0.5;
                BRpower = -0.5;
            }

            if (getruntime() > 16 && getruntime() <= 20 ) {

                if (angles.firstAngle >= 90) {

                    FRpower = 0;
                    FLpower = 0;
                    BRpower = 0;
                    BLpower = 0;

                }
                else if (angles.firstAngle <= 90) {

                    FRpower = 0.3;
                    FLpower = -0.3;
                    BLpower = 0.3;
                    BRpower = -0.3;

                }
            }

            if (getruntime() > 20 && getruntime() <= 24 ) {

                FRpower = 0.5;
                FLpower = 0.5;
                BRpower = 0.5;
                BLpower = 0.5;
            }

            if (getruntime() == 24) {

                 FRpower = 0;
                 FLpower = 0;
                 BRpower = 0;
                 BLpower = 0;
            }

            if (getruntime() > 24 && getruntime() <= 26 ) {

                if (angles.firstAngle >= 90) {

                    FRpower = 0;
                    FLpower = 0;
                    BRpower = 0;
                    BLpower = 0;

                }
                else if (angles.firstAngle <= 90) {

                    FRpower = 0.3;
                    FLpower = -0.3;
                    BLpower = 0.3;
                    BRpower = -0.3;

                }
            }

            if (getruntime() == 26) {

                FRpower = 0;
                FLpower = 0;
                BRpower = 0;
                BLpower = 0;
            }

            if (getruntime() > 26 && getruntime() <= 28 ) {

                FRpower = 0.5;
                FLpower = 0.5;
                BRpower = 0.5;
                BLpower = 0.5;
            }

            if (getruntime() == 28) {

                FRpower = 0;
                FLpower = 0;
                BRpower = 0;
                BLpower = 0;
            }

            if (getruntime() > 28 && getruntime() <= 29) {

                LIMpower = 0;
                RIMpower = 1;
                TRIMpower = 0;
                TLIMpower = 1;
            }

            if (getruntime() > 29 && getruntime() >= 30) {

                FRpower = 0;
                FLpower = 0;
                BRpower = 0;
                BLpower = 0;
            }








            /*if (runtimeme2< 20) {

                    RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);

                    if (vuMark == RelicRecoveryVuMark.UNKNOWN) {

                        telemetry.addData("VuMark", "is NOT visible");
                    } else if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
                        if (vuMark == RelicRecoveryVuMark.LEFT) {

                            telemetry.addData("VuMark", "is Left", vuMark);
                            vuforiaPosition = LEFT;
                        } else if (vuMark == RelicRecoveryVuMark.CENTER) {

                            telemetry.addData("VuMark", "is Center", vuMark);
                            vuforiaPosition = CENTER;
                        } else if (vuMark == RelicRecoveryVuMark.RIGHT) {

                            telemetry.addData("VuMark", "is Right", vuMark);
                            vuforiaPosition = RIGHT;
                        } else if (vuMark == RelicRecoveryVuMark.UNKNOWN) {

                            telemetry.addData("VuMark", "is NOT visible", vuMark);
                        }
                    }
            }


            if (runtimeme2 < 4){
                    FRpower = 0;
                    FLpower = 0;
                    BLpower = 0;
                    BRpower = 0;
                }
            else if (runtimeme2>4 && runtimeme2<7){
                    FRpower = 0;
                    FLpower = 0;
                    BLpower = 0;
                    BRpower = 0;
                    jewelServo.setPosition(0);
                    if (hsvValues[0] < 35 && sensorColor.red() > sensorColor.blue()){
                        telemetry.addLine("This is red");
                        red = true;

                    }

                    else if (hsvValues[0] >= 35 && sensorColor.red() <sensorColor.blue()){
                        telemetry.addLine("This is blue");
                        blue = true;
                    }

                    else{
                        telemetry.addLine("What am I doing with my life?");
                        neither = true;
                    }
                }

                if (runtimeme2 > 7 &&  runtimeme2 <7.4)

                {
                    if (red == blue){
                        FRpower = 0;
                        FLpower = 0;
                        BLpower = 0;
                        BRpower = 0;
                        neither = true;
                    }
                    else {
                        neither = false;
                    }
                }

                else if (runtimeme2 > 7.4 &&  runtimeme2 <=7.7) {
                    if (blue==true){

                        FRpower = -0.3;
                        FLpower = 0.3;
                        BLpower = -0.3;
                        BRpower = 0.3;
                    }
                    else if (red==true){
                        FRpower = 0.3;
                        FLpower = -0.3;
                        BLpower = 0.3;
                        BRpower = -0.3;

                    }

                    else{
                        FRpower = 0;
                        FLpower = 0;
                        BLpower = 0;
                        BRpower = 0;
                    }}


                else if (runtimeme2 > 7.8 &&  runtimeme2 <=8.2){

                    FRpower = 0;
                    FLpower = 0;
                    BLpower = 0;
                    BRpower = 0;
                    A_ServoValue = 1;

                }
                else if (runtimeme2 > 8.2 &&  runtimeme2 <=8.5) {


                    if (blue==true){

                        FRpower = 0.3;
                        FLpower = -0.3;
                        BLpower = 0.3;
                        BRpower = -0.3;

                    }
                    else if (red==true){

                        FRpower = -0.3;
                        FLpower = 0.3;
                        BLpower = -0.3;
                        BRpower = 0.3;

                    }}
                else if (runtimeme2 > 8.55 &&  runtimeme2 <=12.3){

                    FRpower = 0;
                    FLpower = 0;
                    BLpower = 0;
                    BRpower = 0;
                }
                else if (runtimeme2 > 12.3 && runtimeme2 <= 18) {

                    if (runtimeme2 > 12.3 && runtimeme2 <= 12.3 + .3) {
                        FRpower = .3;
                        FLpower = .3;
                        BLpower = .3;
                        BRpower =.3;

                    }

                    else{
                        FRpower = 0;
                        FLpower = 0;
                        BLpower = 0;
                        BRpower = 0;

                    }}
                else if (runtimeme2 > 18&& runtimeme2 <= 23){
                    if (angles.firstAngle>=-83){
                        FRpower = -.3;
                        FLpower = .3;
                        BLpower = -.3;
                        BRpower = .3;
                    }
                    else{
                        FRpower = 0;
                        FLpower = 0;
                        BLpower = 0;
                        BRpower = 0;
                    }
                }
                else if (runtimeme2 > 23 && runtimeme2 <= 24){
                    FRpower = 0;
                    FLpower = 0;
                    BLpower = 0;
                    BRpower = 0;
                }
                else if (runtimeme2 > 24 && runtimeme2 <= 25.5){
                    FRpower = 0;
                    FLpower = 0;
                    BLpower = 0;
                    BRpower = 0;


                }

                else if (runtimeme2 > 25.5&& runtimeme2 <=27.2 ){
                    FRpower = -.3;
                    FLpower = -.3;
                    BLpower = -.3;
                    BRpower =-.5;
                }
                else if (runtimeme2 > 27.2&& runtimeme2 <=27.6 ){
                    FRpower = .3;
                    FLpower = .3;
                    BLpower = .3;
                    BRpower = .3;
                }
                else if (runtimeme2 > 27.6 && runtimeme2 <=28.5 ){
                    FRpower = 0;
                    FLpower = 0;
                    BLpower = 0;
                    BRpower = 0;

                }
                else{
                    FRpower = 0;
                    FLpower = 0;
                    BLpower = 0;
                    BRpower = 0;

                }







                if (runtimeme2 < 3) {
                    A_ServoValue = .1;

                } else if (runtimeme2 > 3 && runtimeme2 < 8) {
                    A_ServoValue = .6;
                } else if (runtimeme2 > 8&& runtimeme2 < 25) {
                    A_ServoValue = .1;
                }
                else{
                    A_ServoValue = .1;
                }*/



                //motors
                frontLeft.setPower(FLpower);
                frontRight.setPower(FRpower);
                backLeft.setPower(BLpower);
                backRight.setPower(BRpower);
                //servos
                jewelServo.setPosition(A_ServoValue);
                glyphLeft.setPosition(LIMpower);
                glyphRight.setPosition(RIMpower);
                topGlyphLeft.setPosition(TLIMpower);
                topGlyphRight.setPosition(TRIMpower);

            }
        /*relativeLayout.post(new Runnable() {
            public void run() {
                relativeLayout.setBackgroundColor(Color.WHITE);
            }
        });*/


            /*while (Math.abs(robotAngle - target) > 3 && opModeIsActive()) {  //Continue while the robot direction is further than three degrees from the target
                if (robotAngle > target) {  //if gyro is positive, we will turn right

                    telemetry.update();
                    frontRight.setPower(-0.25);
                    frontLeft.setPower(0.25);
                    backRight.setPower(-0.25);
                    backLeft.setPower(0.25);
                }

                if (robotAngle < target) {  //if gyro is positive, we will turn left

                    telemetry.update();
                    frontRight.setPower(0.25);
                    frontLeft.setPower(-0.25);
                    backRight.setPower(0.25);
                    backLeft.setPower(-0.25);
                }

                angles = imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);  //Set variables to gyro readings
                robotAngle = angles.firstAngle;
                telemetry.update();
            }*/

            /*angles = imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            telemetry.update();
            if (angles.firstAngle>=-84){
                telemetry.update();
                frontRight.setPower(-0.25);
                frontLeft.setPower(0.25);
                backRight.setPower(-0.25);
                backLeft.setPower(0.25);
            }
            else{
                telemetry.update();
                frontRight.setPower(0);
                frontLeft.setPower(0);
                backRight.setPower(0);
                backLeft.setPower(0);
            }
*/


            /*runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < 1.0)) {

                frontLeft.setPower(0.5);
                frontRight.setPower(0.5);
                backLeft.setPower(0.5);
                backRight.setPower(0.5);
            }
            runtime.reset();

            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);

            *//*hile (opModeIsActive() && angles.firstAngle != -90){

                frontRight.setPower(-0.5);
                frontLeft.setPower(0.5);
                backRight.setPower(-0.5);
                backLeft.setPower(0.5);
            }
            robotAngle = angles.secondAngle;
*//*
            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);

            runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < 1.0)) {

                frontLeft.setPower(-0.5);
                frontRight.setPower(-0.5);
                backLeft.setPower(-0.5);
                backRight.setPower(-0.5);
            }
            runtime.reset();

            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);

            jewelServo.setPosition(0);
            if (hsvValues[0] < 35 && sensorColor.red() > sensorColor.blue()){
                telemetry.addLine("This is red");
                runtime.reset();
                while (opModeIsActive() && (runtime.seconds() < 0.5)) {

                    frontLeft.setPower(0.5);
                    frontRight.setPower(-0.5);
                    backLeft.setPower(0.5);
                    backRight.setPower(-0.5);
                }
                runtime.reset();

                while (opModeIsActive() && (runtime.seconds() < 0.5)) {

                    frontLeft.setPower(-0.5);
                    frontRight.setPower(0.5);
                    backLeft.setPower(-0.5);
                    backRight.setPower(0.5);
                }
                runtime.reset();
            }


            else if (hsvValues[0] >= 35 && sensorColor.red() <sensorColor.blue()){
                telemetry.addLine("This is blue");
                runtime.reset();
                while (opModeIsActive() && (runtime.seconds() < 0.5)) {

                    frontLeft.setPower(-0.5);
                    frontRight.setPower(0.5);
                    backLeft.setPower(-0.5);
                    backRight.setPower(0.5);
                }
                runtime.reset();

                while (opModeIsActive() && (runtime.seconds() < 0.5)) {

                    frontLeft.setPower(0.5);
                    frontRight.setPower(-0.5);
                    backLeft.setPower(0.5);
                    backRight.setPower(-0.5);
                }
                runtime.reset();
            }

            else{
                telemetry.addLine("What am I doing with my life?");
            }
            jewelServo.setPosition(1);

            while (opModeIsActive() && robotAngle - 90 == robotAngle){

                frontRight.setPower(-0.5);
                frontLeft.setPower(0.5);
                backRight.setPower(-0.5);
                backLeft.setPower(0.5);
            }
            robotAngle = angles.secondAngle;

            runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < 1.0)) {

                frontLeft.setPower(0.5);
                frontRight.setPower(0.5);
                backLeft.setPower(0.5);
                backRight.setPower(0.5);
            }
            runtime.reset();

            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);

            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);

            if (vuMark != RelicRecoveryVuMark.UNKNOWN) {

                telemetry.addData("VuMark", "is visible", vuMark);
            }

            else if (vuMark == RelicRecoveryVuMark.UNKNOWN) {

                telemetry.addData("VuMark", "is NOT visible", vuMark);
            }

            if (vuMark == RelicRecoveryVuMark.LEFT) {

                telemetry.addData("VuMark", "is Left", vuMark);
                vuforiaPosition = LEFT;
            }

            else if (vuMark == RelicRecoveryVuMark.CENTER) {

                telemetry.addData("VuMark", "is Center", vuMark);
                vuforiaPosition = CENTER;
            }

            else if (vuMark == RelicRecoveryVuMark.RIGHT) {

                telemetry.addData("VuMark", "is Right", vuMark);
                vuforiaPosition = RIGHT;
            }

            while (opModeIsActive() && robotAngle + 90 == robotAngle){

                frontRight.setPower(0.5);
                frontLeft.setPower(-0.5);
                backRight.setPower(0.5);
                backLeft.setPower(-0.5);
            }
            robotAngle = angles.secondAngle;

            runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < vuforiaPosition)) {

                frontLeft.setPower(0.5);
                frontRight.setPower(0.5);
                backLeft.setPower(0.5);
                backRight.setPower(0.5);
            }
            runtime.reset();

            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);*/

            /*while (opModeIsActive() && robotAngle + 90 == robotAngle){

                frontRight.setPower(0.5);
                frontLeft.setPower(-0.5);
                backRight.setPower(0.5);
                backLeft.setPower(-0.5);
            }
            robotAngle = angles.secondAngle;*/

           /* runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < 4)) {

                frontLeft.setPower(0.5);
                frontRight.setPower(0.5);
                backLeft.setPower(0.5);
                backRight.setPower(0.5);
            }
            runtime.reset();

            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);

            glyphLeft.setPosition(0.7); // 0.7 is the open position for glyph servo Left
            glyphRight.setPosition(0.3); // 0.3 is the open position for glyph servo Right
*/

            /*runtime.reset();
            while (opModeIsActive() && runtime.seconds() < 30){

            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);}*/

        }

        // Set the panel back to the default color





    //----------------------------------------------------------------------------------------------
    // Telemetry Configuration
    //----------------------------------------------------------------------------------------------

    void composeTelemetry() {

        // At the beginning of each telemetry update, grab a bunch of data
        // from the IMU that we will then display in separate lines.
        telemetry.addAction(new Runnable() { @Override public void run()
        {
            // Acquiring the angles is relatively expensive; we don't want
            // to do that in each of the three items that need that info, as that's
            // three times the necessary expense.
            angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            gravity  = imu.getGravity();
        }
        });

        telemetry.addLine()
                .addData("status", new Func<String>() {
                    @Override public String value() {
                        return imu.getSystemStatus().toShortString();
                    }
                })
                .addData("calib", new Func<String>() {
                    @Override public String value() {
                        return imu.getCalibrationStatus().toString();
                    }
                });

        telemetry.addLine()
                .addData("heading", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.firstAngle);
                    }
                })
                .addData("roll", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.secondAngle);
                    }
                })
                .addData("pitch", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.thirdAngle);
                    }
                });

        telemetry.addLine()
                .addData("grvty", new Func<String>() {
                    @Override public String value() {
                        return gravity.toString();
                    }
                })
                .addData("mag", new Func<String>() {
                    @Override public String value() {
                        return String.format(Locale.getDefault(), "%.3f",
                                Math.sqrt(gravity.xAccel*gravity.xAccel
                                        + gravity.yAccel*gravity.yAccel
                                        + gravity.zAccel*gravity.zAccel));
                    }
                });
    }

    //----------------------------------------------------------------------------------------------
    // Formatting
    //----------------------------------------------------------------------------------------------

    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees(double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }





}


