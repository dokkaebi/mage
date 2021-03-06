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

package mage.abilities.effects.common;

import mage.abilities.Ability;
import mage.abilities.effects.OneShotEffect;
import mage.cards.Card;
import mage.constants.Outcome;
import mage.constants.Zone;
import mage.filter.common.FilterLandCard;
import mage.game.Game;
import mage.players.Player;
import mage.target.Target;
import mage.target.common.TargetCardInHand;

/**
 *
 * @author LevelX2
 */
public class PutLandFromHandOntoBattlefieldEffect extends OneShotEffect {

    private boolean tapped;

    public PutLandFromHandOntoBattlefieldEffect() {
        this(false);
    }
    public PutLandFromHandOntoBattlefieldEffect(boolean tapped) {
        super(Outcome.PutLandInPlay);
        staticText = "you may put a land card from your hand onto the battlefield" + (tapped ? " tapped":"");
    }

    public PutLandFromHandOntoBattlefieldEffect(final PutLandFromHandOntoBattlefieldEffect effect) {
        super(effect);
        this.tapped = effect.tapped;
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player controller = game.getPlayer(source.getControllerId());
        if (controller != null) {
            Target target = new TargetCardInHand(new FilterLandCard("land card"));
            if (target.canChoose(source.getSourceId(), source.getControllerId(), game)
                    && controller.choose(outcome, target, source.getSourceId(), game)) {
                Card card = game.getCard(target.getFirstTarget());
                if (card != null) {
                    controller.putOntoBattlefieldWithInfo(card, game, Zone.HAND, source.getSourceId(), tapped);
                }
            }
            return true;
        }
        return false;

    }

    @Override
    public PutLandFromHandOntoBattlefieldEffect copy() {
        return new PutLandFromHandOntoBattlefieldEffect(this);
    }

 }
