/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.sets.magic2015;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.TriggeredAbilityImpl;
import mage.abilities.costs.mana.GenericManaCost;
import mage.abilities.effects.Effect;
import mage.abilities.effects.common.CounterUnlessPaysEffect;
import mage.cards.CardImpl;
import mage.constants.CardType;
import mage.constants.Rarity;
import mage.constants.Zone;
import mage.filter.common.FilterControlledCreaturePermanent;
import mage.filter.predicate.mageobject.SubtypePredicate;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.events.GameEvent.EventType;
import mage.game.permanent.Permanent;
import mage.target.TargetStackObject;

/**
 *
 * @author LevelX2
 */
public class DiffusionSliver extends CardImpl {

    public DiffusionSliver(UUID ownerId) {
        super(ownerId, 50, "Diffusion Sliver", Rarity.UNCOMMON, new CardType[]{CardType.CREATURE}, "{1}{U}");
        this.expansionSetCode = "M15";
        this.subtype.add("Sliver");

        this.color.setBlue(true);
        this.power = new MageInt(1);
        this.toughness = new MageInt(1);

        // Whenever a Sliver creature you control becomes the target of a spell or ability an opponent controls, counter that spell or ability unless its controller pays {2}.
        this.addAbility(new DiffusionSliverTriggeredAbility(new CounterUnlessPaysEffect(new GenericManaCost(2))));
    }

    public DiffusionSliver(final DiffusionSliver card) {
        super(card);
    }

    @Override
    public DiffusionSliver copy() {
        return new DiffusionSliver(this);
    }
}

class DiffusionSliverTriggeredAbility extends TriggeredAbilityImpl {

    private static final FilterControlledCreaturePermanent filter = new FilterControlledCreaturePermanent("Sliver creature you control");

    static {
        filter.add(new SubtypePredicate("Sliver"));
    }

    public DiffusionSliverTriggeredAbility(Effect effect) {
        super(Zone.BATTLEFIELD, effect);
    }

    public DiffusionSliverTriggeredAbility(final DiffusionSliverTriggeredAbility ability) {
        super(ability);
    }

    @Override
    public DiffusionSliverTriggeredAbility copy() {
        return new DiffusionSliverTriggeredAbility(this);
    }

    @Override
    public boolean checkTrigger(GameEvent event, Game game) {
        if (event.getType() == EventType.TARGETED && game.getOpponents(this.controllerId).contains(event.getPlayerId())) {
            Permanent creature = game.getPermanent(event.getTargetId());
            if (creature != null && filter.match(creature, getSourceId(), getControllerId(), game)) {
                this.getTargets().clear();
                TargetStackObject target = new TargetStackObject();
                target.add(event.getSourceId(), game);
                this.addTarget(target);
                return true;
            }
        }
        return false;
    }

    @Override
    public String getRule() {
        return "Whenever a Sliver creature you control becomes the target of a spell or ability an opponent controls, counter that spell or ability unless its controller pays {2}.";
    }
}