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
package mage.abilities.mana;

import java.util.List;
import mage.Mana;
import mage.abilities.Ability;
import mage.abilities.costs.Cost;
import mage.abilities.costs.common.TapSourceCost;
import mage.abilities.effects.common.ManaEffect;
import mage.cards.Card;
import mage.choices.Choice;
import mage.choices.ChoiceImpl;
import mage.constants.ColoredManaSymbol;
import mage.constants.Zone;
import mage.game.Game;
import mage.players.Player;

/**
 *
 * @author LevelX2
 */

public class CommanderColorIdentityManaAbility extends ManaAbility {

    public CommanderColorIdentityManaAbility() {
        super(Zone.BATTLEFIELD, new CommanderIdentityManaEffect(),new TapSourceCost());
    }
    
    public CommanderColorIdentityManaAbility(Cost cost) {
        super(Zone.BATTLEFIELD, new CommanderIdentityManaEffect(), cost);
    }
    
    public CommanderColorIdentityManaAbility(final CommanderColorIdentityManaAbility ability) {
        super(ability);
    }

    @Override
    public CommanderColorIdentityManaAbility copy() {
        return new CommanderColorIdentityManaAbility(this);
    }

    @Override
    public List<Mana> getNetMana(Game game) {
        if (netMana.isEmpty()) {
            Player controller = game.getPlayer(getControllerId());
            if (controller != null) {
                Card commander = game.getCard(controller.getCommanderId());
                if (commander != null) {
                    Mana commanderMana = commander.getManaCost().getMana();
                    if (commanderMana.getBlack() > 0) {
                        netMana.add(new Mana(ColoredManaSymbol.B));
                    }
                    if (commanderMana.getBlue() > 0) {
                        netMana.add(new Mana(ColoredManaSymbol.U));
                    }
                    if (commanderMana.getGreen() > 0) {
                        netMana.add(new Mana(ColoredManaSymbol.G));
                    }
                    if (commanderMana.getRed() > 0) {
                        netMana.add(new Mana(ColoredManaSymbol.R));
                    }
                    if (commanderMana.getWhite() > 0) {
                        netMana.add(new Mana(ColoredManaSymbol.W));
                    }
                }
            }
        }
        return netMana;
    }

    @Override
    public boolean definesMana() {
        return true;
    }


}

class CommanderIdentityManaEffect extends ManaEffect {

    public CommanderIdentityManaEffect() {
        super();
        this.staticText = "Add to your mana pool one mana of any color in your commander's color identity";
    }

    public CommanderIdentityManaEffect(final CommanderIdentityManaEffect effect) {
        super(effect);
    }

    @Override
    public CommanderIdentityManaEffect copy() {
        return new CommanderIdentityManaEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player controller = game.getPlayer(source.getControllerId());
        if (controller != null) {
            Card commander = game.getCard(controller.getCommanderId());
            if (commander != null) {
                Mana commanderMana = commander.getManaCost().getMana();
                Choice choice = new ChoiceImpl();
                choice.setMessage("Pick a mana color");
                if (commanderMana.getBlack() > 0) {
                    choice.getChoices().add("Black");
                }
                if (commanderMana.getRed() > 0) {
                    choice.getChoices().add("Red");
                }
                if (commanderMana.getBlue() > 0) {
                    choice.getChoices().add("Blue");
                }
                if (commanderMana.getGreen() > 0) {
                    choice.getChoices().add("Green");
                }
                if (commanderMana.getWhite() > 0) {
                    choice.getChoices().add("White");
                }
                if (choice.getChoices().size() > 0) {
                    if (choice.getChoices().size() == 1) {
                        choice.setChoice(choice.getChoices().iterator().next());
                    } else {
                        if (!controller.choose(outcome, choice, game)) {
                            return false;
                        }
                    }
                    Mana mana = new Mana();
                    switch (choice.getChoice()) {
                        case "Black":
                            mana.setBlack(1);
                            break;
                        case "Blue":
                            mana.setBlue(1);
                            break;
                        case "Red":
                            mana.setRed(1);
                            break;
                        case "Green":
                            mana.setGreen(1);
                            break;
                        case "White":
                            mana.setWhite(1);
                            break;
                    }
                    checkToFirePossibleEvents(mana, game, source);
                    controller.getManaPool().addMana(mana, game, source);                    
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Mana getMana(Game game, Ability source) {
        return null;
    }
}
