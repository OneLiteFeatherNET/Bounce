package net.theevilreaper.bounce.listener.game;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import net.minestom.server.MinecraftServer;
import net.theevilreaper.bounce.attribute.AttributeHelper;
import net.theevilreaper.bounce.event.BounceGameFinishEvent;
import net.theevilreaper.bounce.player.BouncePlayer;
import net.theevilreaper.bounce.util.GameMessages;

import java.util.List;
import java.util.function.Consumer;

public class GameFinishListener implements Consumer<BounceGameFinishEvent> {

    @Override
    public void accept(BounceGameFinishEvent event) {
        if (event.getReason() == BounceGameFinishEvent.Reason.PLAYER_LEFT) return;

        List<BouncePlayer> participants = MinecraftServer.getConnectionManager().getOnlinePlayers().stream()
                .map(BouncePlayer.class::cast)
                .filter(BouncePlayer::isRoundActive)
                .toList();

        BouncePlayer winner = participants.stream()
                .max(BouncePlayer::compareStanding)
                .orElse(null);

        if (winner == null) {
            participants.forEach(BouncePlayer::endRound);
            return;
        }

        Component displayName = winner.getDisplayName();
        Title title = Title.title(displayName, GameMessages.WON_COMPONENT, Title.DEFAULT_TIMES);

        for (BouncePlayer participant : participants) {
            participant.endRound();
            AttributeHelper.resetJumpStrength(participant);
            boolean isWinner = participant.equals(winner);
            participant.sendStats(isWinner);
            participant.sendTitlePart(TitlePart.TITLE, title.title());
            participant.sendTitlePart(TitlePart.SUBTITLE, title.subtitle());
            participant.sendTitlePart(TitlePart.TIMES, title.times());
        }
    }
}
