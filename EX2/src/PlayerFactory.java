/**
 * Player Factory class.
 * this class is a factory to create a player by types.
 */
public class PlayerFactory {

    /**
     * this function builds the game player according to the given type.
     * "human" - represents HumanPlayer
     * "clever" - represents CleverPlayer
     * "genius" - represents GeniusPlayer
     * "whatever" - represents WhateverPlayer
     * @param type - String represents the type of the player
     * @return Player of HumanPlayer or GeniusPlayer or WhateverPlayer or CleverPlayer. null otherwise.
     */
    public Player buildPlayer(String type){
        switch (type) {
            case "human":
                return new HumanPlayer();
            case "clever":
                return new CleverPlayer();
            case "genius":
                return new GeniusPlayer();
            case "whatever":
                return new WhateverPlayer();
            default:
                return null;
        }
    }

}
