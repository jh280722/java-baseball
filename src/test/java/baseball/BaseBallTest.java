/*
 * BaseBallTest
 *
 * 0.4
 *
 * 2020.12.31
 *
 * 저작권 주의 by huey.j
 */

package baseball;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static java.time.Duration.ofMillis;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

public class BaseBallTest {
    int[] com_num = new int[3];
    int[] user_num = new int[3];
    boolean[] used_num = new boolean[10];
    boolean end;
    boolean error;

    @Test
    @DisplayName("랜덤으로 만들어지는 수 테스트")
    public void initTest(){
        init();
        assertThat(com_num[0]).isGreaterThan(0).isLessThan(10);
        assertThat(com_num[1]).isGreaterThan(0).isLessThan(10);
        assertThat(com_num[2]).isGreaterThan(0).isLessThan(10);

        assertThat(com_num[0]).isNotEqualTo(com_num[1]).isNotEqualTo(com_num[2]);
        assertThat(com_num[1]).isNotEqualTo(com_num[0]).isNotEqualTo(com_num[2]);
        assertThat(com_num[2]).isNotEqualTo(com_num[0]).isNotEqualTo(com_num[1]);
    }

    private void init() {
        end = false;
        error = false;

        //숫자의 사용여부 저장 배열
        for(int i=0;i<10;i++)
            used_num[i] = false;

        //컴퓨터의 숫자 3개 랜덤으로 생성
        for(int i=0;i<3;i++){
            com_num[i] = random();
            user_num[i] = 0;
        }

    }

    private int random() {
        int num = (int) (Math.random() * 9 + 1);  // 1 ~ 9까지 수를 랜덤으로 생성

        while(used_num[num]){
            num = (int) (Math.random() * 9 + 1);  // 1 ~ 9까지 수를 랜덤으로 생성
        }

        used_num[num] = true;   // num이 사용되었음을 체크

        return num;
    }

    private int parseString() {
        int input;

        //Test를 위한 입력 변경
//        Scanner sc = new Scanner(System.in);
//        String Sinput = sc.nextLine();

        String Sinput = "123";
        //Sinput = "1235";
        //Sinput = "558";
        //Sinput = "846";
        //Sinput = "qwe1";

        try {
            input = Integer.parseInt(Sinput);
        } catch (NumberFormatException e) {
            error = true;
            return -1;
        }

        return input;
    }

    private boolean checkError(int input) {
        //3자리 이상인 수 확인
        if(input > 0)
            return true;

        //중복 확인
        return user_num[0] == user_num[1] || user_num[0] == user_num[2] || user_num[1] == user_num[2];
    }

    @Test
    @DisplayName("유저가 입력한 숫자 파싱하여 저장된 값 테스트")
    public void scanNumTest() {
        int input = parseString();

        for(int i=2;i>=0;i--){
            user_num[i] = input % 10;
            input /= 10;
        }

        if(checkError(input)) {
            error = true;
        }

        assertThat(error).isFalse();
    }


    private int getStrike() {
        int strike = 0;

        if(com_num[0] == user_num[0])
            strike++;
        if(com_num[1] == user_num[1])
            strike++;
        if(com_num[2] == user_num[2])
            strike++;

        return strike;
    }

    private int getBall() {
        int ball = 0;

        if(user_num[0] == com_num[1] || user_num[0] == com_num[2])
            ball++;
        if(user_num[1] == com_num[0] || user_num[1] == com_num[2])
            ball++;
        if(user_num[2] == com_num[0] || user_num[2] == com_num[1])
            ball++;

        return ball;
    }

    @Test
    @DisplayName("볼 개수 파악 로직 테스트")
    public void checkBallTest() {
        int res;
        //int[] com = {1, 2, 3};
        //int[] user = {1, 2, 3};

        //int[] com = {1, 2, 3};
        //int[] user = {3, 2, 1};

        int[] com = {1, 2, 3};
        int[] user = {4, 2, 5};

        com_num = com.clone();
        user_num = user.clone();

        res = getBall();

        //assertThat(res).isEqualTo(0);
        //assertThat(res).isEqualTo(2);
        assertThat(res).isEqualTo(0);
    }

    @Test
    @DisplayName("스트라이 개수 파악 로직 테스트")
    public void checkStrikeTest() {
        int res;
        //int[] com = {1, 2, 3};
        //int[] user = {1, 2, 3};

        //int[] com = {1, 2, 3};
        //int[] user = {3, 2, 1};

        int[] com = {1, 2, 3};
        int[] user = {4, 2, 5};

        com_num = com.clone();
        user_num = user.clone();

        res = getStrike();

        //assertThat(res).isEqualTo(3);
        //assertThat(res).isEqualTo(1);
        assertThat(res).isEqualTo(1);

    }

    private String getResultTest(int strike, int ball) {

        String res = "";

        if(strike > 0)
            res += strike + "스트라이크 ";
        if(ball > 0)
            res += ball + "볼";
        if(strike == 0 && ball == 0)
            res += "낫싱";

        return res;
    }

    @Test
    @DisplayName("결과 출력 테스트")
    public void printResultTest() {
        if(error)
            return;

        //int[] com = {1, 2, 3};
        //int[] user = {3, 2, 5};

        int[] com = {3, 2, 1};
        int[] user = {3, 2, 1};

        com_num = com.clone();
        user_num = user.clone();

        int strike = getStrike();
        int ball = getBall();

        String res = getResultTest(strike,ball);

        System.out.println(res);

        if(strike == 3){
            end = true;
            System.out.println("3개의 숫자를 모두 맞히셨습니다! 게임 종료");
        }
    }

    private boolean loopCheck() {
        error = false;

        //int input = 1; // 1일 경우 Timeout 2000까지 반복
        int input = 2; //2이고 end = false 일때 반복

        //input = 2, end = true 일때 정상 종료
        //int input = 2;
        //end = true;

        if(!end)
            return true;
        System.out.println("게임을 새로 시작하려면 1, 종료하려면 다른 숫자를 입력하세요.");

        //Scanner sc = new Scanner(System.in);
        //int input = sc.nextInt();


        if(input == 1){
            init();
            return true;
        }
        return false;
    }

    @Test
    @DisplayName("입력에 따른 loopCheck 테스트")
    public void loopTest() {
        //init();

        assertTimeoutPreemptively(ofMillis(2000), () -> {
            while(loopCheck()) {
                //System.out.print("숫자를 입력해주세요 : ");
                //scanNumTest();
                //printResultTest();
            }
        });
    }
}
