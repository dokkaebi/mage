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
package mage.sets.saviorsofkamigawa;

import java.util.UUID;
import mage.MageObject;
import mage.abilities.Ability;
import mage.abilities.common.AsEntersBattlefieldAbility;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.effects.ContinuousRuleModifiyingEffectImpl;
import mage.abilities.effects.common.NameACardEffect;
import mage.cards.CardImpl;
import mage.constants.AbilityType;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.constants.Outcome;
import mage.constants.Rarity;
import mage.constants.Zone;
import mage.game.Game;
import mage.game.events.GameEvent;

/**
 *
 * @author jeffwadsworth, nox
 */
public class PithingNeedle extends CardImpl {

    public PithingNeedle(UUID ownerId) {
        super(ownerId, 158, "Pithing Needle", Rarity.RARE, new CardType[]{CardType.ARTIFACT}, "{1}");
        this.expansionSetCode = "SOK";

        // As Pithing Needle enters the battlefield, name a card.
        this.addAbility(new AsEntersBattlefieldAbility(new NameACardEffect(NameACardEffect.TypeOfName.ALL)));
        
        // Activated abilities of sources with the chosen name can't be activated unless they're mana abilities.
        this.addAbility(new SimpleStaticAbility(Zone.BATTLEFIELD, new PithingNeedleEffect()));
    }

    public PithingNeedle(final PithingNeedle card) {
        super(card);
    }

    @Override
    public PithingNeedle copy() {
        return new PithingNeedle(this);
    }
}

class PithingNeedleEffect extends ContinuousRuleModifiyingEffectImpl {

    public PithingNeedleEffect() {
        super(Duration.WhileOnBattlefield, Outcome.Detriment);
        staticText = "Activated abilities of sources with the chosen name can't be activated unless they're mana abilities";
    }

    public PithingNeedleEffect(final PithingNeedleEffect effect) {
        super(effect);
    }

    @Override
    public PithingNeedleEffect copy() {
        return new PithingNeedleEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        return true;
    }

    @Override
    public boolean applies(GameEvent event, Ability source, Game game) {
        if (event.getType() == GameEvent.EventType.ACTIVATE_ABILITY) {
            MageObject object = game.getObject(event.getSourceId());
            Ability ability = game.getAbility(event.getTargetId(), event.getSourceId());
            if (ability != null && object != null) {
                if (ability.getAbilityType() != AbilityType.MANA &&
                    object.getName().equals(game.getState().getValue(source.getSourceId().toString() + NameACardEffect.INFO_KEY))) {
                        return true;
                }
            }
        }
        return false;
    }
}
