package me.megaalex.inncore.credits;

import java.math.BigDecimal;
import java.util.UUID;

import org.bukkit.scheduler.BukkitRunnable;

import me.megaalex.inncore.messages.Message;
import me.megaalex.inncore.utils.PlayerUtils;

public class ChangeTask extends BukkitRunnable {

    public enum ChangeType {
        SEND(1),
        GRANT(2),
        DEDUCT(3),
        SET(4);

        private int type;

        ChangeType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    private ChangeType change;

    private UUID executorId;
    private String executorName;
    private String receiverName;


    private BigDecimal creditsChange;

    public ChangeTask(ChangeType change, UUID executorId, String executorName,
                      String receiverName, BigDecimal creditsChange) {
        setValues(change, executorId, executorName, receiverName, creditsChange);
    }

    public ChangeTask(ChangeType change, UUID executorId, String executorName,
                      String receiverName, double creditsChange) {
        setValues(change, executorId, executorName,
                receiverName, new BigDecimal(creditsChange).setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    private void setValues(ChangeType change, UUID executorId, String executorName,
                           String receiverName, BigDecimal creditsChange) {
        this.change = change;

        this.executorId = executorId;
        this.executorName = executorName;

        this.receiverName = receiverName;

        this.creditsChange = creditsChange;
    }


    @Override
    public void run() {

        if(this.creditsChange.compareTo(BigDecimal.ZERO) <= 0) {
            PlayerUtils.sendMessage(executorId,
                    Message.NEGATIVE_ARG);
            return;
        }

        if(this.change == ChangeType.SEND) {
            if(!CreditsManager.has(executorName, creditsChange)) {
                PlayerUtils.sendMessage(executorId,
                        Message.CANT_SEND, creditsChange.toPlainString());
                return;
            }

            if(!CreditsManager.deduct(executorName, creditsChange)) {
                PlayerUtils.sendMessage(executorId,
                        Message.ERROR, creditsChange.toPlainString());
                return;
            }
        }

        if(this.change == ChangeType.DEDUCT) {
            if(!CreditsManager.deduct(receiverName, creditsChange)) {
                PlayerUtils.sendMessage(executorId,
                        Message.ERROR, creditsChange.toPlainString());
                return;
            }
        } else if(this.change == ChangeType.SET) {
            if(!CreditsManager.set(receiverName, creditsChange)) {
                PlayerUtils.sendMessage(executorId,
                        Message.ERROR, creditsChange.toPlainString());
                return;
            }
        } else {
            if(!CreditsManager.grant(receiverName, creditsChange)) {
                PlayerUtils.sendMessage(executorId,
                        Message.ERROR, creditsChange.toPlainString());
                if(this.change == ChangeType.SEND)
                    CreditsManager.grant(executorName, creditsChange);
                return;
            }
        }

        Message msgTypeExecutor, msgTypeReceiver = null;
        if(this.change == ChangeType.SEND) {
            msgTypeExecutor = Message.SENT;
            msgTypeReceiver = Message.RECEIVED;
        } else if(this.change == ChangeType.GRANT) {
            msgTypeExecutor = Message.GIVEN;
            msgTypeReceiver = Message.RECEIVED;
        } else if(this.change == ChangeType.SET) {
            msgTypeExecutor = Message.SET;
        } else {
            msgTypeExecutor = Message.TAKEN;
            msgTypeReceiver = Message.TOOK;
        }

        Long time = System.currentTimeMillis() / 1000L;
        TransactionData transactionData = new TransactionData(executorName, receiverName, creditsChange, time, change.getType());
        CreditsManager.saveTransaction(transactionData);

        PlayerUtils.sendMessage(executorId, msgTypeExecutor,
                receiverName, creditsChange.toPlainString());
        if(this.change != ChangeType.SET)
            PlayerUtils.sendMessage(receiverName, msgTypeReceiver,
                    executorName, creditsChange.toPlainString());
    }
}
