package script.conversation;

import script.library.ai_lib;
import script.library.chat;
import script.library.groundquests;
import script.library.static_item;
import script.library.sui;
import script.library.utils;
import script.*;

public class prof_force_sensitive_main extends script.base_script
{
    public prof_force_sensitive_main()
    {
    }
    public static String c_stringFile = "conversation/prof_force_sensitive_main";

    public static final String CRYSTAL_BLUE = "item_color_crystal_02_04";
    public static final String CRYSTAL_GREEN = "item_color_crystal_02_02";
    public static final String CRYSTAL_YELLOW = "item_color_crystal_02_06";
    public static final String CRYSTAL_RED = "item_color_crystal_02_00";



    public boolean prof_force_sensitive_main_condition__defaultCondition(obj_id player, obj_id npc) throws InterruptedException
    {
        return true;
    }
    public boolean prof_force_sensitive_main_condition_playerOnFS11Reward(obj_id player, obj_id npc) throws InterruptedException
    {
        return groundquests.isTaskActive(player, "prof_force_sensitive_11", "getreward");
    }
    public boolean prof_force_sensitive_main_condition_playerfinishedFS11Quest(obj_id player, obj_id npc) throws InterruptedException
    {
        return groundquests.hasCompletedQuest(player, "prof_force_sensitive_11");
    }
    public boolean prof_force_sensitive_main_condition_playeronFS21(obj_id player, obj_id npc) throws InterruptedException
    {
        return groundquests.isQuestActive(player, "prof_force_sensitive_21_1");
    }
    public boolean prof_force_sensitive_main_condition_playerOnFS21Reward(obj_id player, obj_id npc) throws InterruptedException
    {
        return groundquests.isTaskActive(player, "prof_force_sensitive_21_1", "getreward");
    }
    public void prof_force_sensitive_main_action_giveRewardSignal_11(obj_id player, obj_id npc) throws InterruptedException
    {
        groundquests.sendSignal(player, "prof_force_sensitive_11_reward");
    }
    public void prof_force_sensitive_main_action_facePlayer(obj_id player, obj_id npc) throws InterruptedException
    {
        faceTo(npc, player);
    }
	
	
    public void prof_force_sensitive_main_action_giveRewardSignal_21(obj_id player, obj_id npc) throws InterruptedException
    {
       showCrystalChoice(player, npc);
    }
    
    public void showCrystalChoice(obj_id player, obj_id npc) throws InterruptedException
{
    if (!groundquests.isTaskActive(player, "prof_force_sensitive_21_1", "getreward"))
    {
        return;
    }

    String[] options = new String[4];
    options[0] = "Blue";
    options[1] = "Green";
    options[2] = "Yellow";
    options[3] = "Red";

    int pid = sui.listbox(
        npc,
        player,
        "You have found your crystal. Choose the color that reflects your path.",
        sui.OK_CANCEL,
        "Choose Your Lightsaber Crystal",
        options,
        "handleCrystalChoice",
        true,
        false
    );

    sui.showSUIPage(pid);
}

public int handleCrystalChoice(obj_id self, dictionary params) throws InterruptedException
{
    obj_id player = sui.getPlayerId(params);

    if (!isIdValid(player))
    {
        return SCRIPT_CONTINUE;
    }

    if (sui.getIntButtonPressed(params) == sui.BP_CANCEL)
    {
        return SCRIPT_CONTINUE;
    }

    int selection = sui.getListboxSelectedRow(params);

    if (selection < 0)
    {
        return SCRIPT_CONTINUE;
    }

    if (!groundquests.isTaskActive(player, "prof_force_sensitive_21_1", "getreward"))
    {
        return SCRIPT_CONTINUE;
    }

    String crystal = "";

    switch (selection)
    {
        case 0:
            crystal = CRYSTAL_BLUE;
            break;
        case 1:
            crystal = CRYSTAL_GREEN;
            break;
        case 2:
            crystal = CRYSTAL_YELLOW;
            break;
        case 3:
            crystal = CRYSTAL_RED;
            break;
        default:
            return SCRIPT_CONTINUE;
    }

    obj_id pInv = utils.getInventoryContainer(player);
    static_item.createNewItemFunction(crystal, pInv);

    groundquests.sendSignal(player, "prof_force_sensitive_21_3_reward");

    return SCRIPT_CONTINUE;
}

    public int prof_force_sensitive_main_handleBranch1(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        if (response.equals("s_13"))
        {
            if (prof_force_sensitive_main_condition__defaultCondition(player, npc))
            {
                doAnimationAction(npc, "nod");
                prof_force_sensitive_main_action_giveRewardSignal_21(player, npc);
                string_id message = new string_id(c_stringFile, "s_14");
                utils.removeScriptVar(player, "conversation.prof_force_sensitive_main.branchId");
                npcEndConversationWithMessage(player, message);
                return SCRIPT_CONTINUE;
            }
        }
        return SCRIPT_DEFAULT;
    }
    public int prof_force_sensitive_main_handleBranch3(obj_id player, obj_id npc, string_id response) throws InterruptedException
    {
        if (response.equals("s_20"))
        {
            if (prof_force_sensitive_main_condition__defaultCondition(player, npc))
            {
                doAnimationAction(npc, "bow");
                prof_force_sensitive_main_action_giveRewardSignal_11(player, npc);
                string_id message = new string_id(c_stringFile, "s_22");
                utils.removeScriptVar(player, "conversation.prof_force_sensitive_main.branchId");
                npcEndConversationWithMessage(player, message);
                return SCRIPT_CONTINUE;
            }
        }
        if (response.equals("s_24"))
        {
            doAnimationAction(player, "pose_proudly");
            if (prof_force_sensitive_main_condition__defaultCondition(player, npc))
            {
                prof_force_sensitive_main_action_giveRewardSignal_11(player, npc);
                string_id message = new string_id(c_stringFile, "s_26");
                utils.removeScriptVar(player, "conversation.prof_force_sensitive_main.branchId");
                npcEndConversationWithMessage(player, message);
                return SCRIPT_CONTINUE;
            }
        }
        return SCRIPT_DEFAULT;
    }
    public int OnInitialize(obj_id self) throws InterruptedException
    {
        if ((!isMob(self)) || (isPlayer(self)))
        {
            detachScript(self, "conversation.prof_force_sensitive_main");
        }
        setCondition(self, CONDITION_CONVERSABLE);
        setCondition(self, CONDITION_INTERESTING);
        setInvulnerable(self, true);
        setName(self, "Sol Windtide");
        ai_lib.setDefaultCalmBehavior(self, ai_lib.BEHAVIOR_SENTINEL);
        return SCRIPT_CONTINUE;
    }
    public int OnAttach(obj_id self) throws InterruptedException
    {
        setCondition(self, CONDITION_CONVERSABLE);
        setCondition(self, CONDITION_INTERESTING);
        setInvulnerable(self, true);
        setName(self, "Sol Windtide");
        ai_lib.setDefaultCalmBehavior(self, ai_lib.BEHAVIOR_SENTINEL);
        return SCRIPT_CONTINUE;
    }
    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info menuInfo) throws InterruptedException
    {
        int menu = menuInfo.addRootMenu(menu_info_types.CONVERSE_START, null);
        menu_info_data menuInfoData = menuInfo.getMenuItemById(menu);
        menuInfoData.setServerNotify(false);
        setCondition(self, CONDITION_CONVERSABLE);
        return SCRIPT_CONTINUE;
    }
    public int OnIncapacitated(obj_id self, obj_id killer) throws InterruptedException
    {
        clearCondition(self, CONDITION_CONVERSABLE);
        detachScript(self, "conversation.prof_force_sensitive_main");
        return SCRIPT_CONTINUE;
    }
    public boolean npcStartConversation(obj_id player, obj_id npc, String convoName, string_id greetingId, prose_package greetingProse, string_id[] responses) throws InterruptedException
    {
        Object[] objects = new Object[responses.length];
        System.arraycopy(responses, 0, objects, 0, responses.length);
        return npcStartConversation(player, npc, convoName, greetingId, greetingProse, objects);
    }
    public int OnStartNpcConversation(obj_id self, obj_id player) throws InterruptedException
    {
        obj_id npc = self;
        if (ai_lib.isInCombat(npc) || ai_lib.isInCombat(player))
        {
            return SCRIPT_OVERRIDE;
        }
        if (prof_force_sensitive_main_condition_playerOnFS21Reward(player, npc))
        {
            string_id message = new string_id(c_stringFile, "s_12");
            int numberOfResponses = 0;
            boolean hasResponse = false;
            boolean hasResponse0 = false;
            if (prof_force_sensitive_main_condition__defaultCondition(player, npc))
            {
                ++numberOfResponses;
                hasResponse = true;
                hasResponse0 = true;
            }
            if (hasResponse)
            {
                int responseIndex = 0;
                string_id responses[] = new string_id[numberOfResponses];
                if (hasResponse0)
                {
                    responses[responseIndex++] = new string_id(c_stringFile, "s_13");
                }
                utils.setScriptVar(player, "conversation.prof_force_sensitive_main.branchId", 1);
                npcStartConversation(player, npc, "prof_force_sensitive_main", message, responses);
            }
            else 
            {
                chat.chat(npc, player, message);
            }
            return SCRIPT_CONTINUE;
        }
        if (prof_force_sensitive_main_condition_playerOnFS11Reward(player, npc))
        {
            prof_force_sensitive_main_action_facePlayer(player, npc);
            string_id message = new string_id(c_stringFile, "s_18");
            int numberOfResponses = 0;
            boolean hasResponse = false;
            boolean hasResponse0 = false;
            if (prof_force_sensitive_main_condition__defaultCondition(player, npc))
            {
                ++numberOfResponses;
                hasResponse = true;
                hasResponse0 = true;
            }
            boolean hasResponse1 = false;
            if (prof_force_sensitive_main_condition__defaultCondition(player, npc))
            {
                ++numberOfResponses;
                hasResponse = true;
                hasResponse1 = true;
            }
            if (hasResponse)
            {
                int responseIndex = 0;
                string_id responses[] = new string_id[numberOfResponses];
                if (hasResponse0)
                {
                    responses[responseIndex++] = new string_id(c_stringFile, "s_20");
                }
                if (hasResponse1)
                {
                    responses[responseIndex++] = new string_id(c_stringFile, "s_24");
                }
                utils.setScriptVar(player, "conversation.prof_force_sensitive_main.branchId", 3);
                prose_package pp = new prose_package();
                pp.stringId = message;
                pp.actor.set(player);
                pp.target.set(npc);
                npcStartConversation(player, npc, "prof_force_sensitive_main", null, pp, responses);
            }
            else 
            {
                prose_package pp = new prose_package();
                pp.stringId = message;
                pp.actor.set(player);
                pp.target.set(npc);
                chat.chat(npc, player, null, null, pp);
            }
            return SCRIPT_CONTINUE;
        }
        if (prof_force_sensitive_main_condition_playeronFS21(player, npc))
        {
            string_id message = new string_id(c_stringFile, "s_15");
            chat.chat(npc, player, message);
            return SCRIPT_CONTINUE;
        }
        if (prof_force_sensitive_main_condition_playerfinishedFS11Quest(player, npc))
        {
            prof_force_sensitive_main_action_facePlayer(player, npc);
            string_id message = new string_id(c_stringFile, "s_17");
            chat.chat(npc, player, message);
            return SCRIPT_CONTINUE;
        }
        if (prof_force_sensitive_main_condition__defaultCondition(player, npc))
        {
            prof_force_sensitive_main_action_facePlayer(player, npc);
            string_id message = new string_id(c_stringFile, "s_28");
            chat.chat(npc, player, message);
            return SCRIPT_CONTINUE;
        }
        chat.chat(npc, "Error:  All conditions for OnStartNpcConversation were false.");
        return SCRIPT_CONTINUE;
    }
    public int OnNpcConversationResponse(obj_id self, String conversationId, obj_id player, string_id response) throws InterruptedException
    {
        if (!conversationId.equals("prof_force_sensitive_main"))
        {
            return SCRIPT_CONTINUE;
        }
        obj_id npc = self;
        int branchId = utils.getIntScriptVar(player, "conversation.prof_force_sensitive_main.branchId");
        if (branchId == 1 && prof_force_sensitive_main_handleBranch1(player, npc, response) == SCRIPT_CONTINUE)
        {
            return SCRIPT_CONTINUE;
        }
        if (branchId == 3 && prof_force_sensitive_main_handleBranch3(player, npc, response) == SCRIPT_CONTINUE)
        {
            return SCRIPT_CONTINUE;
        }
        chat.chat(npc, "Error:  Fell through all branches and responses for OnNpcConversationResponse.");
        utils.removeScriptVar(player, "conversation.prof_force_sensitive_main.branchId");
        return SCRIPT_CONTINUE;
    }
}
