package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import org.firstinspires.ftc.robotcore.external.JavaUtil;

@TeleOp(name = "OneControllerRizzedUp")
public class OneControllerRizzedUp extends LinearOpMode {

    private Servo armservo;
    private DcMotor ArmMotor;
    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor leftRear;
    private DcMotor rightRear;

    double ticks = 1680;
    double newTarget;

    public boolean clawOpen = true;
    public boolean clawPressed = false;



    /**
     * This function is executed when this Op Mode is selected from the Driver Station.
     */
    @Override
    public void runOpMode() {
        double speedE;
        double forward;
        double strafe;
        double turn;
        double denominator;
        double armPowerVariable;
        double arm_power;




        armservo = hardwareMap.get(Servo.class, "arm servo!");
        ArmMotor = hardwareMap.get(DcMotor.class, "Arm Motor");
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        leftRear = hardwareMap.get(DcMotor.class, "leftRear");
        rightRear = hardwareMap.get(DcMotor.class, "rightRear");

        // Put initialization blocks here.
        waitForStart();
        // 0.2 before 0.1 to try make open wider
        armservo.setPosition(0.1);

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                // Put loop blocks here.
                // if (gamepad1.left_bumper)
                // {
                //   encoder(2);
                // }

                //set speed
                if (gamepad1.right_bumper) {
                    armPowerVariable = 0.3;
                    speedE = 0.25;
                }
                else {
                    speedE = 1;
                    armPowerVariable = 2;
                }



                //servo
                if ((gamepad1.a && !clawPressed)) {
                    clawOpen = !clawOpen;
                    if (clawOpen)
                    {
                        armservo.setPosition(0.35);
                    }
                    else{
                        armservo.setPosition(0.25);
                    }

                }
                clawPressed = gamepad1.a;
                telemetry.addData("servoPos", armservo.getPosition());
                telemetry.update();
                // arm movement

                if(gamepad1.dpad_up){
                    arm_power = 0.5 * armPowerVariable;
                }
                else if (gamepad1.dpad_down){
                    arm_power = -0.25 * armPowerVariable;
                }
                else{
                    arm_power = 0;
                }

                ArmMotor.setPower(arm_power);
                telemetry.addData("armPos", ArmMotor.getCurrentPosition());
                telemetry.update();

                // movement

                forward = gamepad1.left_stick_y * -speedE;
                strafe = -(gamepad1.right_stick_x * speedE);
                turn = gamepad1.right_stick_x * speedE;
                denominator = JavaUtil.maxOfList(JavaUtil.createListWith(1, Math.abs(forward) + Math.abs(strafe) + Math.abs(turn)));
                telemetry.addData("forward power", forward);
                //movement
                leftFront.setPower((forward - (strafe + turn)) / denominator);
                rightFront.setPower((forward - (strafe - turn)) / denominator);
                leftRear.setPower((forward + (strafe - turn)) / denominator);
                rightRear.setPower((forward + strafe + turn) / denominator);
                // changed +,- to -,- (this fixed the movement controls)

                telemetry.update();

            }
        }

    }

}

// public void encoder(int turnage){
//   ArmMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//   newTarget = ticks/turnage;
//   ArmMotor.setTargetPosition((int)newTarget);
//   ArmMotor.setPower(0.3);
//   ArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
// }

