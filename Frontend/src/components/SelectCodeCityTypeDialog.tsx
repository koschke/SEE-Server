import { Box, Button, Modal, Stack, SxProps, Theme, Typography } from "@mui/material";
import { useState } from "react";
import ProjectType, { ProjectTypeUtils } from "../types/ProjectType";

type Props = {
  isOpen: boolean;
  onClose?: (projectType?: ProjectType) => void;
  style?: SxProps<Theme>;
}

const modalStyle: SxProps<Theme> = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  maxWidth: "1200px",
  minWidth: "400px",
  bgcolor: 'background.paper',
  borderRadius: "25px",
  boxShadow: 24,
  p: 4,
};



/**
 * A dialog component that allows users to select a project type from a list of available project types.
 *
 * @component
 * @param {boolean} props.isOpen - Determines whether the dialog is open or closed.
 * @param {function} [props.onClose] - Callback function triggered when the dialog is closed.
 *                                      Receives the selected project type or `undefined` if no selection is made.
 * @param {object} [props.style] - Custom styles to apply to the modal container.
 *
 * @returns {JSX.Element} The rendered dialog component.
 *
 * @example
 * ```tsx
 * <SelectCodeCityTypeDialog
 *   isOpen={true}
 *   onClose={(selectedType) => {
 *     if (selectedType) {
 *       console.log("Selected project type:", selectedType);
 *     } else {
 *       console.log("Dialog closed without selection.");
 *     }
 *   }}
 *   style={{ backgroundColor: "white", padding: "20px" }}
 * />
 * ```
 */
function SelectCodeCityTypeDialog({ isOpen, onClose = () => { }, style = modalStyle }: Props) {

  type ConditionalProjectType = [ProjectType, boolean];
  const [availableProjectTypes] = useState<ConditionalProjectType[]>(ProjectTypeUtils.getAll().map(projectType => [projectType, true]));

  return (
    <Modal
      open={isOpen}
      onClose={() => {
        onClose(undefined);
      }}
      aria-labelledby="remove-user-modal-title"
      aria-describedby="remove-user-modal-description">
      <Box sx={style}>
        <Typography id="modal-title" variant="h6" sx={{ marginBottom: "6pt" }}>
          Select Code City Type
        </Typography>
        <Stack direction="column" spacing={1}>
          {availableProjectTypes.filter(([, active]) => active === true).map(([projectType]) =>
            <Button key={projectType} variant="contained" color="primary" sx={{ borderRadius: "25px", width: "100%" }} onClick={() => { onClose(projectType); }}>
              {ProjectTypeUtils.getLabel(projectType)}
            </Button>
          )}
        </Stack>
        <Stack justifyContent="end" direction="row" spacing={2} sx={{ marginTop: "2em" }}>
          <Button variant="contained" color="secondary" sx={{ borderRadius: "25px" }} onClick={() => onClose(undefined)}>
            Cancel
          </Button>
        </Stack>
      </Box>
    </Modal>
  );
}

export default SelectCodeCityTypeDialog;
