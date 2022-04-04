package commands;

import data.exercises.ExerciseList;
import data.exercises.InvalidExerciseException;
import data.plans.InvalidPlanException;
import data.plans.PlanList;
import data.schedule.DayList;
import data.schedule.InvalidScheduleException;
import data.workouts.InvalidWorkoutException;
import data.workouts.WorkoutList;
import storage.FileManager;
import storage.LogHandler;
import werkit.Parser;
import werkit.UI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ScheduleCommandTest {
    ExerciseList exerciseList;
    WorkoutList workoutList;
    PlanList planList;
    UI ui;
    Parser parser;
    FileManager fileManager;
    DayList dayList;

    @BeforeEach
    void setUp() throws InvalidWorkoutException, InvalidExerciseException, InvalidPlanException {
        LogHandler.startLogHandler();
        exerciseList = new ExerciseList();
        workoutList = new WorkoutList(exerciseList);
        planList = new PlanList(workoutList);
        ui = new UI();
        fileManager = new FileManager(planList);
        dayList = new DayList(planList);
        parser = new Parser(ui, exerciseList, workoutList, fileManager, planList, dayList);

        exerciseList.addExerciseToList("push up");
        exerciseList.addExerciseToList("sit up");
        exerciseList.addExerciseToList("pull up");

        workoutList.createAndAddWorkout("push up /reps 10");
        workoutList.createAndAddWorkout("sit up /reps 15");
        workoutList.createAndAddWorkout("pull up /reps 20");

        planList.createAndAddPlan("more muscles /workouts 1,2,1,2");
        planList.createAndAddPlan("more arm muscles /workouts 1,3,1,3");
        planList.createAndAddPlan("only core /workouts 2,2,2,2");
    }

    @Test
    void setUserAction_createInvalidAction_expectInvalidCommandException() {
        assertThrows(InvalidCommandException.class, () -> new ScheduleCommand("schedule /invalid command",
                fileManager, dayList, "/invalid command", ""));
    }

    @Test
    public void scheduleCommand_validScheduleUpdateConstruction() throws IOException,
            InvalidCommandException {
        String userInput = "schedule /update 1 2";
        String userAction = "/update";
        String userArguments = "1 2";
        ExerciseList el = new ExerciseList();
        WorkoutList wl = new WorkoutList(el);
        PlanList pl = new PlanList(wl);
        DayList dl = new DayList(pl);
        FileManager fm = new FileManager(pl);

        ScheduleCommand commandTest = new ScheduleCommand(userInput, fm, dl, userAction, userArguments);

        assertEquals("schedule /update 1 2", commandTest.getUserInput());
        assertEquals("/update", commandTest.getUserAction());
        assertEquals("1 2", commandTest.getUserArguments());
    }

    @Test
    public void scheduleCommand_validScheduleClearConstruction() throws IOException,
            InvalidCommandException {
        String userInput = "schedule /clear 1";
        String userAction = "/clear";
        String userArguments = "1";
        ExerciseList el = new ExerciseList();
        WorkoutList wl = new WorkoutList(el);
        PlanList pl = new PlanList(wl);
        DayList dl = new DayList(pl);
        FileManager fm = new FileManager(pl);

        ScheduleCommand commandTest = new ScheduleCommand(userInput, fm, dl, userAction, userArguments);

        assertEquals("schedule /clear 1", commandTest.getUserInput());
        assertEquals("/clear", commandTest.getUserAction());
        assertEquals("1", commandTest.getUserArguments());
    }

    @Test
    public void scheduleCommand_validScheduleClearAllConstruction() throws IOException,
            InvalidCommandException {
        String userInput = "schedule /clearall";
        String userAction = "/clearall";
        String userArguments = " ";
        ExerciseList el = new ExerciseList();
        WorkoutList wl = new WorkoutList(el);
        PlanList pl = new PlanList(wl);
        DayList dl = new DayList(pl);
        FileManager fm = new FileManager(pl);

        ScheduleCommand commandTest = new ScheduleCommand(userInput, fm, dl, userAction, userArguments);

        assertEquals("schedule /clearall", commandTest.getUserInput());
        assertEquals("/clearall", commandTest.getUserAction());
        assertEquals(" ", commandTest.getUserArguments());
    }

}