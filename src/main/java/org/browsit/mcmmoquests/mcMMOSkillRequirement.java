/*
 * Copyright (c) 2021 Browsit, LLC. All rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN
 * NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.browsit.mcmmoquests;

import java.util.AbstractMap;
import java.util.Map;

import com.gmail.nossr50.mcMMO;
import org.bukkit.entity.Player;

import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.datatypes.player.PlayerProfile;
import com.gmail.nossr50.datatypes.skills.PrimarySkillType;
import com.gmail.nossr50.util.player.UserManager;

import me.blackvein.quests.CustomRequirement;

public class mcMMOSkillRequirement extends CustomRequirement {
	
	public mcMMOSkillRequirement() {
		setName("mcMMO Overhaul Skill Requirement");
		setAuthor("Browsit, LLC");
		addItem("DIAMOND_SWORD", (short)1562);
		addStringPrompt("Skill Type", "Name of the skill type", "ANY");
		addStringPrompt("Skill Amount", "Enter the quantity of skill levels to need", "1");
	}

	@Override
	public String getModuleName() {
		return "mcMMO Overhaul Quests Module";
	}

	@Override
	public Map.Entry<String, Short> getModuleItem() {
		return new AbstractMap.SimpleEntry<>("DIAMOND_SWORD", (short)1562);
	}
	
	@Override
	public boolean testRequirement(final Player player, final Map<String, Object> data) {
		if (data != null) {
			if (!UserManager.hasPlayerDataKey(player)) {
				UserManager.track(new McMMOPlayer(player, new PlayerProfile(player.getName(), player.getUniqueId(),
						0)));
			}
			final String skillType = (String)data.getOrDefault("Skill Type", "ANY");
			int skillLevels = 1;
			try {
				skillLevels = Integer.parseInt((String)data.getOrDefault("Skill Amount", "1"));
			} catch (final NumberFormatException e) {
				// Default to 1
			}
			if (skillType.equalsIgnoreCase("ANY")) {
				for (final PrimarySkillType pst : PrimarySkillType.values()) {
					if (UserManager.getPlayer(player).getProfile().getSkillLevel(pst) >= skillLevels) {
						return true;
					}
				}
			} else {
				return UserManager.getPlayer(player.getName()).getProfile().getSkillLevel(mcMMO.p.getSkillTools()
						.matchSkill(skillType)) >= skillLevels;
			}
		}
		return false;
	}
}