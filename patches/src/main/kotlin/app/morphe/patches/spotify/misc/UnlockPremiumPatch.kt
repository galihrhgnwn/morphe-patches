package app.morphe.patches.spotify.misc

import app.revanced.patcher.fingerprint.Fingerprint
import app.revanced.patcher.extensions.InstructionExtensions.addInstruction
import app.revanced.patcher.extensions.InstructionExtensions.addInstructions
import app.revanced.patcher.extensions.InstructionExtensions.addInstructionsWithLabels
import app.revanced.patcher.extensions.InstructionExtensions.getInstruction
import app.revanced.patcher.extensions.InstructionExtensions.removeInstruction
import app.revanced.patcher.extensions.InstructionExtensions.replaceInstruction
import app.revanced.patcher.patch.PatchException
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.util.proxy.mutableTypes.MutableClass
import app.revanced.patcher.util.proxy.mutableTypes.MutableMethod
import app.revanced.util.smali.ExternalLabel
import app.revanced.util.*
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import com.android.tools.smali.dexlib2.iface.reference.TypeReference

internal const val EXTENSION_CLASS_DESCRIPTOR = "Lapp/morphe/extension/spotify/misc/UnlockPremiumPatch;"

@Suppress("unused")
val unlockPremiumPatch = bytecodePatch(
    name = "Unlock Premium",
    description = "Unlocks Spotify Premium features. Server-sided features like downloading songs are still locked."
) {
    compatibleWith(
        "com.spotify.music",
        "9.0.90.1229"
    )

    execute {
        fun MutableClass.publicizeField(fieldName: String) {
            fields.first { it.name == fieldName }.apply {
                accessFlags = accessFlags.toPublicAccessFlags()
            }
        }

        accountAttributeFingerprint.classDef.publicizeField("value_")

        productStateProtoGetMapFingerprint.method.apply {
            val getAttributesMapIndex = indexOfFirstInstructionOrThrow(Opcode.IGET_OBJECT)
            val attributesMapRegister = getInstruction<TwoRegisterInstruction>(getAttributesMapIndex).registerA

            addInstruction(
                getAttributesMapIndex + 1,
                "invoke-static { v$attributesMapRegister }, " +
                        "$EXTENSION_CLASS_DESCRIPTOR->overrideAttributes(Ljava/util/Map;)V"
            )
        }

        buildQueryParametersFingerprint.method.apply {
            val addQueryParameterConditionIndex = indexOfFirstInstructionReversedOrThrow(
                buildQueryParametersFingerprint.stringMatches!!.first().index, Opcode.IF_EQZ
            )

            removeInstruction(addQueryParameterConditionIndex)
        }

        contextMenuViewModelClassFingerprint.method.apply {
            val insertIndex = contextMenuViewModelClassFingerprint.patternMatch!!.startIndex
            val registerUrl = getInstruction<FiveRegisterInstruction>(insertIndex).registerC
            val registerUri = getInstruction<FiveRegisterInstruction>(insertIndex + 2).registerD

            val extensionMethodDescriptor = "Lapp/morphe/extension/spotify/misc/UnlockPremiumPatch;->onContextMenu(Ljava/lang/String;Ljava/lang/String;)V"

            addInstruction(
                insertIndex,
                "invoke-static { v$registerUrl, v$registerUri }, $extensionMethodDescriptor"
            )
        }
    }
}
