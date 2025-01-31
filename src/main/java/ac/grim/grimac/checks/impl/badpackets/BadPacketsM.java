package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.impl.movement.NoSlow;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;

@CheckData(name = "BadPacketsM")
public class BadPacketsM extends PacketCheck {
    boolean sentHeldItem = false;

    public BadPacketsM(GrimPlayer playerData) {
        super(playerData);
    }

    public void onPacketReceive(final PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.HELD_ITEM_CHANGE) { // idle packet // TODO: Fix for 1.9+ clients
            if (sentHeldItem && player.getClientVersion().isOlderThan(ClientVersion.V_1_9)) {
                flagAndAlert();
                player.checkManager.getPostPredictionCheck(NoSlow.class).flagWithSetback(); // Impossible to false, call NoSlow violation to setback
            } else {
                sentHeldItem = true;
            }
        }

        if (WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
            sentHeldItem = false;
        }
    }
}
