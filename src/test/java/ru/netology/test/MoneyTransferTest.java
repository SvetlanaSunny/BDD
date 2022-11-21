package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.BalanceCard;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.getFirstCardNumber;
import static ru.netology.data.DataHelper.getSecondCardNumber;
import static ru.netology.page.BalanceCard.transferFirstCardButton;
import static ru.netology.page.BalanceCard.transferSecondCardButton;

public class MoneyTransferTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val autoInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(autoInfo);
        val verifacationCode = DataHelper.getVerificationCodeFor(autoInfo);
        val balanceCard = verificationPage.validVerify(verifacationCode);
    }

    @Test
    public void shouldTranslationFromFirstToSecond() {
        int amount = 3000;
        val cardBalance = new BalanceCard();
        val firstBalanceCardStart = cardBalance.getFirstCardBalance();
        val secondBalanceCardStart = cardBalance.getFirstCardBalance();
        val translationPage = transferSecondCardButton();
        translationPage.transferMoney(amount, getFirstCardNumber());
        val firstCardBalanceFinish = firstBalanceCardStart - amount;
        val secondCardBalanceFinish = secondBalanceCardStart + amount;

        assertEquals(firstCardBalanceFinish, cardBalance.getFirstCardBalance());
        assertEquals(secondCardBalanceFinish, cardBalance.getSecondCardBalance());
    }

    @Test
    public void shouldTranslationFromSecondToFirst() {
        int amount = 7000;
        val cardBalance = new BalanceCard();
        val firstBalanceCardStart = cardBalance.getFirstCardBalance();
        val secondBalanceCardStart = cardBalance.getSecondCardBalance();
        val translationPage = transferFirstCardButton();
        translationPage.transferMoney(amount, getSecondCardNumber());
        val firstCardBalanceFinish = firstBalanceCardStart + amount;
        val secondCardBalanceFinish = secondBalanceCardStart - amount;


        assertEquals(firstCardBalanceFinish, cardBalance.getFirstCardBalance());
        assertEquals(secondCardBalanceFinish, cardBalance.getSecondCardBalance());
    }

    @Test
    public void shouldTransferFromSecondToSecondCard() {
        int amount = 5000;
        val cardBalance = new BalanceCard();
        val firstCardBalanceStart = cardBalance.getFirstCardBalance();
        val secondCardBalanceStart = cardBalance.getSecondCardBalance();
        val transactionPage = transferFirstCardButton();
        transactionPage.transferMoney(amount, getFirstCardNumber());
        transactionPage.invalidCard();
    }

    @Test
    public void shouldTranslationInsufficientFunds() {
        int amount = 20000;
        val cardBalance = new BalanceCard();
        val firstCardBalanceStart = cardBalance.getFirstCardBalance();
        val secondCardBalanceStart = cardBalance.getSecondCardBalance();
        val transactionPage = transferSecondCardButton();
        transactionPage.transferMoney(amount, getFirstCardNumber());
        transactionPage.errorLimit();
    }

}
